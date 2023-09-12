package com.runjian.alarm.service.impl;

import com.runjian.alarm.constant.AlarmFileType;
import com.runjian.alarm.constant.PhotoState;
import com.runjian.alarm.constant.VideoState;
import com.runjian.alarm.dao.AlarmMsgInfoMapper;
import com.runjian.alarm.dao.AlarmSchemeInfoMapper;
import com.runjian.alarm.dao.relation.AlarmSchemeChannelRelMapper;
import com.runjian.alarm.dao.relation.AlarmSchemeEventRelMapper;
import com.runjian.alarm.entity.AlarmMsgInfo;
import com.runjian.alarm.entity.AlarmSchemeInfo;
import com.runjian.alarm.entity.relation.AlarmSchemeChannelRel;
import com.runjian.alarm.entity.relation.AlarmSchemeEventRel;
import com.runjian.alarm.feign.TimerUtilsApi;
import com.runjian.alarm.service.AlarmMsgSouthService;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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

    private final RedissonClient redissonClient;

    private final TimerUtilsApi timerUtilsApi;

    private final static String DEFAULT_UPLOAD_ADDRESS = "/";

    @Override
    public void receiveAlarmMsg(Long channelId, String eventCode, Integer eventEndType, String eventDesc, LocalDateTime eventTime) {
        // 缓存过滤
        RLock redisLock = redissonClient.getLock(eventCode + MarkConstant.MARK_SPLIT_SEMICOLON + channelId);

        if (redisLock.tryLock()) {
            Optional<AlarmSchemeInfo> alarmSchemeInfoOp = alarmSchemeInfoMapper.selectByChannelId(channelId);
            if (alarmSchemeInfoOp.isEmpty()){
                redisLock.unlock();
                return;
            }
            AlarmSchemeInfo alarmSchemeInfo = alarmSchemeInfoOp.get();
            if (CommonEnum.getBoolean(alarmSchemeInfo.getDisabled())){
                redisLock.unlock();
                return;
            }
            Optional<AlarmSchemeEventRel> alarmSchemeEventRelOp = alarmSchemeEventRelMapper.selectBySchemeIdAndEventCode(alarmSchemeInfo.getId(), eventCode);
            if (alarmSchemeEventRelOp.isEmpty()){
                redisLock.unlock();
                return;
            }
            CommonResponse<Boolean> response;
            try{
                response = timerUtilsApi.checkTime(alarmSchemeInfo.getTemplateId(), eventTime);
                if (response.isError() || Objects.isNull(response.getData())){
                    log.error(LogTemplate.ERROR_LOG_TEMPLATE, "告警信息南向服务", "时间校验异常", response);
                    redisLock.unlock();
                }
                if (!response.getData()){
                    redisLock.unlock();
                    return;
                }
            }catch (Exception ex){
                log.error(LogTemplate.ERROR_LOG_TEMPLATE, "告警信息南向服务", "时间校验异常", ex);
                redisLock.unlock();
                return;
            }
            LocalDateTime nowTime = LocalDateTime.now();
            AlarmSchemeEventRel alarmSchemeEventRel = alarmSchemeEventRelOp.get();
            AlarmMsgInfo alarmMsgInfo = new AlarmMsgInfo();
            alarmMsgInfo.setChannelId(channelId);
            alarmMsgInfo.setAlarmLevel(alarmSchemeEventRel.getEventLevel());
            alarmMsgInfo.setAlarmType(alarmSchemeEventRel.getEventCode());
            alarmMsgInfo.setAlarmTime(eventTime);
            alarmMsgInfo.setAlarmDesc(eventDesc);
            alarmMsgInfo.setCreateTime(nowTime);
            alarmMsgInfo.setUpdateTime(nowTime);
            long intervalTime = alarmSchemeEventRel.getEventInterval();
            if (CommonEnum.getBoolean(alarmSchemeEventRel.getEnableVideo())){
                alarmMsgInfo.setVideoState(VideoState.INIT.getCode());
                alarmMsgInfo.setVideoLength(alarmSchemeEventRel.getVideoLength());
                alarmMsgInfo.setVideoAudioState(alarmSchemeEventRel.getVideoHasAudio());
                intervalTime += alarmSchemeEventRel.getVideoLength();
            }
            if (CommonEnum.getBoolean(alarmSchemeEventRel.getEnablePhoto())){
                alarmMsgInfo.setPhotoState(PhotoState.INIT.getCode());
            }
            alarmMsgInfoMapper.save(alarmMsgInfo);
            // 重新上锁
            redisLock.lock(intervalTime, TimeUnit.SECONDS);
            // todo 添加队列任务进行录制
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
        String filename = alarmMsgInfo.getId() + MarkConstant.MARK_SPLIT_RAIL + alarmMsgInfo.getAlarmTime() + ".";
        String path = DEFAULT_UPLOAD_ADDRESS + "/" + alarmMsgInfo.getChannelId();
        Path filePath;
        switch (AlarmFileType.getByCode(alarmFileType)){
            case PHOTO:
                filePath = Paths.get(path, filename + (Objects.isNull(file.getOriginalFilename()) ? "jpg" : file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)));
                alarmMsgInfo.setPhotoUrl(filePath.toString());
                alarmMsgInfo.setVideoState(VideoState.SUCCESS.getCode());
                break;
            case VIDEO:
                filePath = Paths.get(path, filename + (Objects.isNull(file.getOriginalFilename()) ? "mp4" : file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)));
                alarmMsgInfo.setVideoUrl(filePath.toString());
                alarmMsgInfo.setPhotoState(PhotoState.SUCCESS.getCode());
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
