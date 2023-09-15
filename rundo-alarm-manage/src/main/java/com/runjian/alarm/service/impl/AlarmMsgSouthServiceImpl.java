package com.runjian.alarm.service.impl;

import com.runjian.alarm.constant.*;
import com.runjian.alarm.dao.AlarmMsgInfoMapper;
import com.runjian.alarm.dao.AlarmSchemeInfoMapper;
import com.runjian.alarm.dao.relation.AlarmSchemeEventRelMapper;
import com.runjian.alarm.entity.AlarmMsgInfo;
import com.runjian.alarm.entity.AlarmSchemeInfo;
import com.runjian.alarm.entity.relation.AlarmSchemeEventRel;
import com.runjian.alarm.feign.TimerUtilsApi;
import com.runjian.alarm.service.AlarmMsgSouthService;
import com.runjian.alarm.utils.RedisLockUtil;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Miracle
 * @date 2023/9/11 17:12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmMsgSouthServiceImpl implements AlarmMsgSouthService {

    private final AlarmMsgInfoMapper alarmMsgInfoMapper;

    private final AlarmSchemeInfoMapper alarmSchemeInfoMapper;

    private final AlarmSchemeEventRelMapper alarmSchemeEventRelMapper;

    private final StringRedisTemplate redisTemplate;

    private final TimerUtilsApi timerUtilsApi;

    private final RedisLockUtil redisLockUtil;

    private final static String DEFAULT_UPLOAD_ADDRESS = "/";
    
    private final static String UNLOCK_PWD = "1";

    private final static int DEFAULT_SINGLE_MSG_END = 15;

    @Override
    public void receiveAlarmMsg(Long channelId, String eventCode, Integer eventMsgTypeCode, String eventDesc, LocalDateTime eventTime) {
        String lockKey = eventCode + MarkConstant.MARK_SPLIT_SEMICOLON + channelId;
        // 缓存过滤
        EventMsgType eventMsgType = EventMsgType.getByCode(eventMsgTypeCode);
        switch (eventMsgType){
            case SINGLE:
            case COMPOUND_START:
                if (redisLockUtil.lock(lockKey,  UNLOCK_PWD, DEFAULT_SINGLE_MSG_END, TimeUnit.SECONDS, 1)) {
                    Optional<AlarmSchemeInfo> alarmSchemeInfoOp = alarmSchemeInfoMapper.selectByChannelId(channelId);
                    if (alarmSchemeInfoOp.isEmpty()){
                        redisLockUtil.unLock(lockKey, UNLOCK_PWD);
                        return;
                    }
                    AlarmSchemeInfo alarmSchemeInfo = alarmSchemeInfoOp.get();
                    if (CommonEnum.getBoolean(alarmSchemeInfo.getDisabled())){
                        redisLockUtil.unLock(lockKey, UNLOCK_PWD);
                        return;
                    }
                    Optional<AlarmSchemeEventRel> alarmSchemeEventRelOp = alarmSchemeEventRelMapper.selectBySchemeIdAndEventCode(alarmSchemeInfo.getId(), eventCode);
                    if (alarmSchemeEventRelOp.isEmpty()){
                        redisLockUtil.unLock(lockKey, UNLOCK_PWD);
                        return;
                    }
                    CommonResponse<Boolean> response;
                    try{
                        response = timerUtilsApi.checkTime(alarmSchemeInfo.getTemplateId(), eventTime);
                        if (response.isError() || Objects.isNull(response.getData())){
                            log.error(LogTemplate.ERROR_LOG_TEMPLATE, "告警信息南向服务", "时间校验异常", response);
                            redisLockUtil.unLock(lockKey, UNLOCK_PWD);
                        }
                        if (!response.getData()){
                            redisLockUtil.unLock(lockKey, UNLOCK_PWD);
                            return;
                        }
                    }catch (Exception ex){
                        log.error(LogTemplate.ERROR_LOG_TEMPLATE, "告警信息南向服务", "时间校验异常", ex);
                        redisLockUtil.unLock(lockKey, UNLOCK_PWD);
                        return;
                    }
                    LocalDateTime nowTime = LocalDateTime.now();
                    AlarmSchemeEventRel alarmSchemeEventRel = alarmSchemeEventRelOp.get();
                    AlarmMsgInfo alarmMsgInfo = new AlarmMsgInfo();
                    alarmMsgInfo.setChannelId(channelId);
                    alarmMsgInfo.setAlarmLevel(alarmSchemeEventRel.getEventLevel());
                    alarmMsgInfo.setAlarmType(alarmSchemeEventRel.getEventCode());
                    alarmMsgInfo.setAlarmStartTime(eventTime);
                    alarmMsgInfo.setAlarmDesc(eventDesc);
                    alarmMsgInfo.setCreateTime(nowTime);
                    alarmMsgInfo.setUpdateTime(nowTime);
                    alarmMsgInfo.setAlarmInterval(alarmSchemeEventRel.getEventInterval());
                    alarmMsgInfo.setAlarmEndTime(eventTime.plusSeconds(alarmSchemeEventRel.getVideoLength()));
                    // 判断是否开启视频录制
                    if (CommonEnum.getBoolean(alarmSchemeEventRel.getEnableVideo())){
                        alarmMsgInfo.setVideoLength(alarmSchemeEventRel.getVideoLength());
                        alarmMsgInfo.setVideoAudioState(alarmSchemeEventRel.getVideoHasAudio());
                        // 判断是否是直到事件结束的录制时间
                        if (Objects.equals(alarmSchemeEventRel.getVideoLength(), 0)){
                            // 判断是否单条信息
                            if (Objects.equals(EventMsgType.SINGLE, eventMsgType)){
                                alarmMsgInfo.setAlarmState(AlarmState.SUCCESS.getCode());
                                alarmMsgInfo.setAlarmEndTime(eventTime.plusSeconds(DEFAULT_SINGLE_MSG_END));
                                alarmMsgInfo.setVideoState(AlarmFileState.WAITING.getCode());
                                redisTemplate.expire(lockKey, alarmMsgInfo.getAlarmInterval() + DEFAULT_SINGLE_MSG_END, TimeUnit.SECONDS);
                            } else {
                                alarmMsgInfo.setAlarmState(AlarmState.UNDERWAY.getCode());
                                alarmMsgInfo.setVideoState(AlarmFileState.INIT.getCode());
                                // 设置5分钟超时时间
                                redisTemplate.opsForValue().set(lockKey, String.valueOf(alarmMsgInfo.getId()), 5, TimeUnit.MINUTES);
                            }
                        } else {
                            alarmMsgInfo.setAlarmState(AlarmState.SUCCESS.getCode());
                            alarmMsgInfo.setVideoState(AlarmFileState.WAITING.getCode());
                            redisTemplate.expire(lockKey, alarmMsgInfo.getAlarmInterval() + alarmSchemeEventRel.getVideoLength(), TimeUnit.SECONDS);
                        }
                    }
                    // 判断是否开启截图
                    if (CommonEnum.getBoolean(alarmSchemeEventRel.getEnablePhoto())){
                        alarmMsgInfo.setImageState(AlarmFileState.WAITING.getCode());
                    }
                    alarmMsgInfoMapper.save(alarmMsgInfo);
                }
                return;
            case COMPOUND_HEARTBEAT:
                // 延迟锁过期
                String alarmMsgInfoId1 = redisTemplate.opsForValue().get(lockKey);
                if (Objects.isNull(alarmMsgInfoId1)){
                    return;
                }
                Optional<AlarmMsgInfo> alarmMsgInfoOp1 = alarmMsgInfoMapper.selectById(Long.valueOf(alarmMsgInfoId1));
                if (alarmMsgInfoOp1.isEmpty()){
                    return;
                }
                LocalDateTime nowTime = LocalDateTime.now();
                AlarmMsgInfo alarmMsgInfo1 = alarmMsgInfoOp1.get();
                alarmMsgInfo1.setUpdateTime(nowTime);
                alarmMsgInfo1.setAlarmEndTime(nowTime);
                alarmMsgInfoMapper.update(alarmMsgInfo1);
                redisTemplate.expire(lockKey, 5, TimeUnit.MINUTES);
                return;
            case COMPOUND_END:
                String alarmMsgInfoId2 = redisTemplate.opsForValue().get(lockKey);
                if (Objects.isNull(alarmMsgInfoId2)){
                    return;
                }
                Optional<AlarmMsgInfo> alarmMsgInfoOp2 = alarmMsgInfoMapper.selectById(Long.valueOf(alarmMsgInfoId2));
                if (alarmMsgInfoOp2.isEmpty()){
                    return;
                }
                AlarmMsgInfo alarmMsgInfo2 = alarmMsgInfoOp2.get();
                // 判断事件是否被提前结束了，提前结束也直接返回
                if (Objects.equals(AlarmState.SUCCESS.getCode(), alarmMsgInfo2.getAlarmState())){
                    return;
                }
                // 设置告警状态为成功
                alarmMsgInfo2.setAlarmState(AlarmState.SUCCESS.getCode());
                alarmMsgInfo2.setUpdateTime(LocalDateTime.now());
                alarmMsgInfo2.setAlarmEndTime(eventTime);
                // 判断录像状态是否是初始化
                if (Objects.equals(AlarmFileState.INIT.getCode(), alarmMsgInfo2.getVideoState())){
                    alarmMsgInfo2.setVideoState(AlarmFileState.WAITING.getCode());
                }
                alarmMsgInfoMapper.update(alarmMsgInfo2);
                // 设置下次间隔时间
                redisTemplate.expire(lockKey, alarmMsgInfo2.getAlarmInterval(), TimeUnit.SECONDS);
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAlarmFile(Long alarmMsgId, Integer alarmFileType, MultipartFile file) {
        Optional<AlarmMsgInfo> alarmMsgInfoOp = alarmMsgInfoMapper.selectById(alarmMsgId);
        if (alarmMsgInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "未找到该报警信息");
        }

        AlarmMsgInfo alarmMsgInfo = alarmMsgInfoOp.get();
        redisLockUtil.unLock(MarkConstant.REDIS_ALARM_MSG_EVENT_LOCK + alarmMsgInfo.getChannelId());
        String filename = alarmMsgInfo.getId() + MarkConstant.MARK_SPLIT_RAIL + alarmMsgInfo.getAlarmStartTime() + ".";
        String path = DEFAULT_UPLOAD_ADDRESS + "/" + alarmMsgInfo.getChannelId();
        Path filePath;
        switch (AlarmFileType.getByCode(alarmFileType)){
            case IMAGE:
                filePath = Paths.get(path, filename + (Objects.isNull(file.getOriginalFilename()) ? "jpg" : file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)));
                alarmMsgInfo.setImageUrl(filePath.toString());
                alarmMsgInfo.setVideoState(AlarmFileState.SUCCESS.getCode());
                break;
            case VIDEO:
                filePath = Paths.get(path, filename + (Objects.isNull(file.getOriginalFilename()) ? "mp4" : file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)));
                alarmMsgInfo.setVideoUrl(filePath.toString());
                alarmMsgInfo.setImageState(AlarmFileState.SUCCESS.getCode());
                break;
            default:
                return;
        }
        alarmMsgInfo.setUpdateTime(LocalDateTime.now());
        alarmMsgInfoMapper.update(alarmMsgInfo);
        try {
            Files.write(filePath, file.getBytes());
        } catch (IOException ex) {
            log.error(LogTemplate.ERROR_LOG_TEMPLATE, "告警信息南向服务", "文件写入本地失败", ex);
            throw new BusinessException(BusinessErrorEnums.UNKNOWN_ERROR, ex.getMessage());
        }
    }


}
