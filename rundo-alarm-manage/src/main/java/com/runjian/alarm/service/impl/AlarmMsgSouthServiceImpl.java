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
import java.util.List;
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
    public void receiveAlarmMsg(Long channelId, String eventCode, Integer eventMsgTypeCode, String eventDesc, LocalDateTime eventTime, Integer hasEventPhoto) {
        String lockKey = eventCode + MarkConstant.MARK_SPLIT_SEMICOLON + channelId;
        // 缓存过滤
        EventMsgType eventMsgType = EventMsgType.getByCode(eventMsgTypeCode);
        switch (eventMsgType){
            case SINGLE:
            case COMPOUND_START:
                if (redisLockUtil.lock(lockKey,  UNLOCK_PWD, 15, TimeUnit.SECONDS, 1)) {
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
                            alarmMsgInfo.setAlarmEndTime(eventTime.plusSeconds(alarmSchemeEventRel.getVideoLength()));
                            alarmMsgInfo.setVideoState(AlarmFileState.WAITING.getCode());
                            redisTemplate.expire(lockKey, alarmMsgInfo.getAlarmInterval() + alarmSchemeEventRel.getVideoLength(), TimeUnit.SECONDS);
                        }
                    }
                    // 判断是否开启截图
                    if (CommonEnum.getBoolean(alarmSchemeEventRel.getEnablePhoto())){
                        alarmMsgInfo.setPhotoState(AlarmFileState.WAITING.getCode());
                        alarmMsgInfo.setPhotoHasExist(hasEventPhoto);
                    }
                    alarmMsgInfoMapper.save(alarmMsgInfo);
                }
                return;
            case COMPOUND_HEARTBEAT:
                redisTemplate.expire(lockKey, 5, TimeUnit.MINUTES);
                return;
            case COMPOUND_END:
                String alarmMsgInfoId = redisTemplate.opsForValue().get(lockKey);
                if (Objects.isNull(alarmMsgInfoId)){
                    return;
                }
                Optional<AlarmMsgInfo> alarmMsgInfoOp = alarmMsgInfoMapper.selectById(Long.valueOf(alarmMsgInfoId));
                if (alarmMsgInfoOp.isEmpty()){
                    return;
                }
                AlarmMsgInfo alarmMsgInfo = alarmMsgInfoOp.get();
                // 判断事件是否被提前结束了，提前结束也直接返回
                if (Objects.equals(AlarmState.SUCCESS.getCode(), alarmMsgInfo.getAlarmState())){
                    return;
                }
                alarmMsgInfo.setAlarmState(AlarmState.SUCCESS.getCode());
                alarmMsgInfo.setUpdateTime(LocalDateTime.now());
                if (Objects.equals(AlarmFileState.INIT.getCode(), alarmMsgInfo.getVideoState())){
                    alarmMsgInfo.setAlarmEndTime(eventTime);
                    alarmMsgInfo.setVideoState(AlarmFileState.WAITING.getCode());
                }
                alarmMsgInfoMapper.update(alarmMsgInfo);
                // 设置下次间隔时间
                redisTemplate.expire(lockKey, alarmMsgInfo.getAlarmInterval(), TimeUnit.SECONDS);
        }
    }

    /**
     * 定时任务
     * 检测正在事件中的告警
     */
    @Override
    public void checkUnderwayAlarm(){
        List<AlarmMsgInfo> alarmMsgInfoList = alarmMsgInfoMapper.selectByAlarmState(AlarmState.UNDERWAY.getCode());
        for (AlarmMsgInfo alarmMsgInfo : alarmMsgInfoList){
            String lockKey = alarmMsgInfo.getAlarmCode() + MarkConstant.MARK_SPLIT_SEMICOLON + alarmMsgInfo.getChannelId();
            if(Objects.nonNull(redisTemplate.opsForValue().get(lockKey))){
                return;
            }
            LocalDateTime nowTime = LocalDateTime.now();
            alarmMsgInfo.setAlarmState(AlarmState.SUCCESS.getCode());
            alarmMsgInfo.setUpdateTime(nowTime);
            if (Objects.equals(AlarmFileState.INIT.getCode(), alarmMsgInfo.getVideoState())) {
                alarmMsgInfo.setAlarmEndTime(nowTime);
                alarmMsgInfo.setVideoState(AlarmFileState.WAITING.getCode());
            }
            alarmMsgInfoMapper.update(alarmMsgInfo);
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
        String filename = alarmMsgInfo.getId() + MarkConstant.MARK_SPLIT_RAIL + alarmMsgInfo.getAlarmStartTime() + ".";
        String path = DEFAULT_UPLOAD_ADDRESS + "/" + alarmMsgInfo.getChannelId();
        Path filePath;
        switch (AlarmFileType.getByCode(alarmFileType)){
            case PHOTO:
                filePath = Paths.get(path, filename + (Objects.isNull(file.getOriginalFilename()) ? "jpg" : file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)));
                alarmMsgInfo.setPhotoUrl(filePath.toString());
                alarmMsgInfo.setVideoState(AlarmFileState.SUCCESS.getCode());
                break;
            case VIDEO:
                filePath = Paths.get(path, filename + (Objects.isNull(file.getOriginalFilename()) ? "mp4" : file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)));
                alarmMsgInfo.setVideoUrl(filePath.toString());
                alarmMsgInfo.setPhotoState(AlarmFileState.SUCCESS.getCode());
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
