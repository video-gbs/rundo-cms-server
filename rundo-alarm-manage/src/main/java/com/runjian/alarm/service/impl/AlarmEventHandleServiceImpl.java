package com.runjian.alarm.service.impl;

import com.runjian.alarm.config.AlarmProperties;
import com.runjian.alarm.constant.AlarmFileState;
import com.runjian.alarm.constant.AlarmFileType;
import com.runjian.alarm.constant.AlarmState;
import com.runjian.alarm.dao.AlarmMsgInfoMapper;
import com.runjian.alarm.dao.relation.AlarmMsgErrorRelMapper;
import com.runjian.alarm.entity.AlarmMsgInfo;
import com.runjian.alarm.entity.relation.AlarmMsgErrorRel;
import com.runjian.alarm.feign.StreamManageApi;
import com.runjian.alarm.service.AlarmEventHandleService;
import com.runjian.alarm.service.AlarmMsgSouthService;
import com.runjian.alarm.utils.RedisLockUtil;
import com.runjian.alarm.vo.request.PostImageDownloadReq;
import com.runjian.alarm.vo.request.PostRecordDownloadReq;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.common.constant.PlayType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Miracle
 * @date 2023/9/15 10:50
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmEventHandleServiceImpl implements AlarmEventHandleService {

    private final AlarmMsgInfoMapper alarmMsgInfoMapper;

    private final AlarmMsgErrorRelMapper alarmMsgErrorRelMapper;

    private final StringRedisTemplate redisTemplate;

    private final RedisLockUtil redisLockUtil;

    private final RedissonClient redissonClient;

    private final StreamManageApi streamManageApi;

    private final AlarmProperties alarmProperties;

    private final static long DELAY_VIDEO_TIME_SECOND = -60;

    private final static long DEFAULT_OUT_TIME_SECOND = 180;

    private final static int DEFAULT_FORWARD_TIME = -5;

    /**
     * 定时任务
     * 检测正在事件中的告警
     */
    @Override
    @Scheduled(fixedDelay = 5000)
    public void checkUnderwayAlarm() {
        RLock rLock = redissonClient.getLock(MarkConstant.REDIS_ALARM_UNDERWAY_LOCK_KEY);
        if (rLock.tryLock()) {
            try {
                List<AlarmMsgInfo> alarmMsgInfoList = alarmMsgInfoMapper.selectByVideoState(AlarmFileState.INIT.getCode());
                if (alarmMsgInfoList.isEmpty()) {
                    return;
                }
                LocalDateTime nowTime = LocalDateTime.now();
                for (AlarmMsgInfo alarmMsgInfo : alarmMsgInfoList) {
                    String lockKey = MarkConstant.REDIS_ALARM_MSG_LOCK + alarmMsgInfo.getAlarmCode() + MarkConstant.MARK_SPLIT_SEMICOLON + alarmMsgInfo.getChannelId();
                    String data = redisTemplate.opsForValue().get(lockKey);
                    if (Objects.equals(data, String.valueOf(alarmMsgInfo.getId()))) {
                        return;
                    }
                    alarmMsgInfo.setUpdateTime(nowTime);
                    if (Objects.equals(AlarmFileState.INIT.getCode(), alarmMsgInfo.getVideoState())) {
                        if(Objects.isNull(alarmMsgInfo.getAlarmEndTime())){
                            alarmMsgInfo.setAlarmEndTime(alarmMsgInfo.getAlarmStartTime().plusSeconds(AlarmMsgSouthService.DEFAULT_SINGLE_MSG_END));
                        }
                        alarmMsgInfo.setVideoState(AlarmFileState.WAITING.getCode());
                    }
                }
                log.error(LogTemplate.ERROR_LOG_TEMPLATE, "告警事件处理服务", "定时检测持续中的告警", alarmMsgInfoList);
                alarmMsgInfoMapper.batchUpdate(alarmMsgInfoList);
            } finally {
                rLock.unlock();
            }
        }
    }

    @Override
    @Scheduled(fixedDelay = 3000)
    public void alarmVideoEventStart() {
        // 视频报警处理
        LocalDateTime nowTime = LocalDateTime.now();
        List<AlarmMsgInfo> alarmMsgInfoList = alarmMsgInfoMapper.selectByVideoStateAndAlarmEndTime(AlarmFileState.WAITING.getCode(), nowTime.plusSeconds(DELAY_VIDEO_TIME_SECOND));
        if (alarmMsgInfoList.isEmpty()) {
            return;
        }
        for (AlarmMsgInfo alarmMsgInfo : alarmMsgInfoList) {
            String lockKey = MarkConstant.REDIS_ALARM_MSG_EVENT_LOCK + alarmMsgInfo.getChannelId();
            Duration duration = Duration.between(alarmMsgInfo.getAlarmStartTime(), alarmMsgInfo.getAlarmEndTime());

            if (redisLockUtil.lock(lockKey, String.format("%s-%s", AlarmFileType.VIDEO.getMsg(), alarmMsgInfo.getId()), duration.getSeconds() + DEFAULT_OUT_TIME_SECOND, TimeUnit.SECONDS, 1)){
                alarmMsgInfo.setUpdateTime(nowTime);
                if (duration.getSeconds() > DEFAULT_OUT_TIME_SECOND){
                    alarmMsgErrorRelMapper.save(getAlarmMsgErrorRel(alarmMsgInfo.getId(), AlarmFileType.VIDEO, "告警视频时长大于3分钟，无法进行下载", nowTime));
                    alarmMsgInfo.setVideoState(AlarmFileState.ERROR.getCode());
                    alarmMsgInfoMapper.update(alarmMsgInfo);
                    redisLockUtil.unLock(lockKey);
                    continue;
                }
                PostRecordDownloadReq postRecordDownloadReq = getPostRecordDownloadReq(alarmMsgInfo);
                try{
                    CommonResponse<String> response = streamManageApi.applyStreamId(postRecordDownloadReq);
                    if(response.isError()){
                        String errorData = String.format("%s:%s", response.getMsg(), response.getData());
                        log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "告警事件处理服务", "下载告警录像请求失败", alarmMsgInfo, errorData);
                        alarmMsgErrorRelMapper.save(getAlarmMsgErrorRel(alarmMsgInfo.getId(), AlarmFileType.VIDEO, errorData, nowTime));
                        redisLockUtil.unLock(lockKey);
                        alarmMsgInfo.setVideoState(AlarmFileState.ERROR.getCode());
                    }else {
                        alarmMsgInfo.setVideoState(AlarmFileState.GENERATING.getCode());
                        alarmMsgInfo.setVideoStreamId(response.getData());
                    }
                } catch (Exception ex){
                    log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "告警事件处理服务", "下载告警录像请求失败", alarmMsgInfo, ex);
                    alarmMsgErrorRelMapper.save(getAlarmMsgErrorRel(alarmMsgInfo.getId(), AlarmFileType.VIDEO, ex.getMessage(), nowTime));
                    redisLockUtil.unLock(lockKey);
                    alarmMsgInfo.setVideoState(AlarmFileState.ERROR.getCode());
                }
                alarmMsgInfoMapper.update(alarmMsgInfo);
            }
        }
    }

    @Override
    @Scheduled(fixedDelay = 1500)
    public void alarmImageEventStart() {
        LocalDateTime nowTime = LocalDateTime.now();
        List<AlarmMsgInfo> alarmMsgInfoList = alarmMsgInfoMapper.selectByImageStateAndAlarmEndTime(AlarmFileState.WAITING.getCode(), nowTime.plusSeconds(DELAY_VIDEO_TIME_SECOND));
        if (alarmMsgInfoList.isEmpty()) {
            return;
        }
        for (AlarmMsgInfo alarmMsgInfo : alarmMsgInfoList) {
            String lockKey = MarkConstant.REDIS_ALARM_MSG_EVENT_LOCK + alarmMsgInfo.getChannelId();
            if (redisLockUtil.lock(lockKey, String.format("%s-%s", AlarmFileType.VIDEO.getMsg(), alarmMsgInfo.getId()), DEFAULT_OUT_TIME_SECOND, TimeUnit.SECONDS, 1)){
                alarmMsgInfo.setUpdateTime(nowTime);
                PostImageDownloadReq postImageDownloadReq = getPostImageDownloadReq(alarmMsgInfo);
                try{
                    CommonResponse<String> response = streamManageApi.applyStreamId(postImageDownloadReq);
                    if(response.isError()){
                        String errorData = String.format("%s:%s", response.getMsg(), response.getData());
                        log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "告警事件处理服务", "下载告警图片请求失败", alarmMsgInfo, errorData);
                        alarmMsgErrorRelMapper.save(getAlarmMsgErrorRel(alarmMsgInfo.getId(), AlarmFileType.IMAGE, errorData, nowTime));
                        redisLockUtil.unLock(lockKey);
                        alarmMsgInfo.setImageState(AlarmFileState.ERROR.getCode());
                    }else {
                        alarmMsgInfo.setImageState(AlarmFileState.GENERATING.getCode());
                    }
                } catch (Exception ex){
                    log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "告警事件处理服务", "下载告警图片请求失败", alarmMsgInfo, ex);
                    alarmMsgErrorRelMapper.save(getAlarmMsgErrorRel(alarmMsgInfo.getId(), AlarmFileType.IMAGE, ex.getMessage(), nowTime));
                    redisLockUtil.unLock(lockKey);
                    alarmMsgInfo.setImageState(AlarmFileState.ERROR.getCode());
                }
                alarmMsgInfoMapper.update(alarmMsgInfo);
            }
        }
    }

    @Override
    @Scheduled(fixedDelay = 5000)
    public void alarmEventCheck(){
        List<AlarmMsgInfo> alarmMsgInfoList = alarmMsgInfoMapper.selectByVideoStateOrImageState(AlarmFileState.GENERATING.getCode());
        if (alarmMsgInfoList.isEmpty()) {
            return;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        for (AlarmMsgInfo alarmMsgInfo : alarmMsgInfoList) {
            String value = String.valueOf(nowTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            if (redisLockUtil.lock(MarkConstant.REDIS_ALARM_MSG_EVENT_CHECK_LOCK + alarmMsgInfo.getChannelId(), value, 3, TimeUnit.SECONDS, 1)) {
                try{
                    String lockValue = redisTemplate.opsForValue().get(MarkConstant.REDIS_ALARM_MSG_EVENT_LOCK + alarmMsgInfo.getChannelId());
                    if (Objects.isNull(lockValue) || (!Objects.equals(lockValue, String.format("%s-%s", AlarmFileType.VIDEO.getMsg(), alarmMsgInfo.getId())) && !Objects.equals(lockValue, String.format("%s-%s", AlarmFileType.IMAGE.getMsg(), alarmMsgInfo.getId())))){
                        if (Objects.equals(alarmMsgInfo.getVideoState(), AlarmFileState.GENERATING.getCode())){
                            alarmMsgInfo.setVideoState(AlarmFileState.ERROR.getCode());
                            alarmMsgErrorRelMapper.save(getAlarmMsgErrorRel(alarmMsgInfo.getId(), AlarmFileType.VIDEO, "告警视频下载超时未完成任务", nowTime));
                            redisLockUtil.unLock(MarkConstant.REDIS_ALARM_MSG_EVENT_LOCK + alarmMsgInfo.getChannelId(), String.format("%s-%s", AlarmFileType.VIDEO.getMsg(), alarmMsgInfo.getId()));
                        }else {
                            alarmMsgInfo.setImageState(AlarmFileState.ERROR.getCode());
                            alarmMsgErrorRelMapper.save(getAlarmMsgErrorRel(alarmMsgInfo.getId(), AlarmFileType.IMAGE, "告警截图下载超时未完成任务", nowTime));
                            redisLockUtil.unLock(MarkConstant.REDIS_ALARM_MSG_EVENT_LOCK + alarmMsgInfo.getChannelId(), String.format("%s-%s", AlarmFileType.IMAGE.getMsg(), alarmMsgInfo.getId()));
                        }
                        alarmMsgInfo.setUpdateTime(nowTime);
                        alarmMsgInfoMapper.update(alarmMsgInfo);
                    }
                }finally {
                    redisLockUtil.unLock(MarkConstant.REDIS_ALARM_MSG_EVENT_CHECK_LOCK + alarmMsgInfo.getChannelId(), value);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recoverAlarmFileHandle(Long alarmMsgId, Integer alarmFileType) {
        Optional<AlarmMsgInfo> alarmMsgInfoOp = alarmMsgInfoMapper.selectById(alarmMsgId);
        if (alarmMsgInfoOp.isEmpty()) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "告警信息不存在");
        }
        AlarmMsgInfo alarmMsgInfo = alarmMsgInfoOp.get();
        switch (AlarmFileType.getByCode(alarmFileType)){
            case VIDEO:
                if (!Objects.equals(alarmMsgInfo.getVideoState(), AlarmFileState.ERROR.getCode())){
                    throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "视频状态非异常，无法恢复");
                }
                alarmMsgInfo.setVideoState(AlarmFileState.WAITING.getCode());
                break;
            case IMAGE:
                if (!Objects.equals(alarmMsgInfo.getImageState(), AlarmFileState.ERROR.getCode())){
                    throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "图片状态非异常，无法恢复");
                }
                alarmMsgInfo.setImageState(AlarmFileState.WAITING.getCode());
                break;
            default:
                throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "告警文件类型不正确");
        }
        alarmMsgInfo.setUpdateTime(LocalDateTime.now());
        alarmMsgInfoMapper.update(alarmMsgInfo);
    }

    private PostRecordDownloadReq getPostRecordDownloadReq(AlarmMsgInfo alarmMsgInfo) {
        PostRecordDownloadReq postRecordDownloadReq = new PostRecordDownloadReq();
        postRecordDownloadReq.setChannelId(alarmMsgInfo.getChannelId());
        if (Objects.nonNull(alarmMsgInfo.getVideoAudioState())){
            postRecordDownloadReq.setEnableAudio(CommonEnum.getBoolean(alarmMsgInfo.getVideoAudioState()));
        }else {
            postRecordDownloadReq.setEnableAudio(false);
        }
        postRecordDownloadReq.setStreamType(2);
        postRecordDownloadReq.setPlayType(PlayType.ALARM.getCode());
        postRecordDownloadReq.setStartTime(alarmMsgInfo.getAlarmStartTime().plusSeconds(DEFAULT_FORWARD_TIME));
        postRecordDownloadReq.setEndTime(alarmMsgInfo.getAlarmEndTime());
        postRecordDownloadReq.setUploadId(String.valueOf(alarmMsgInfo.getId()));
        postRecordDownloadReq.setUploadUrl(alarmProperties.getUploadUrl());
        return postRecordDownloadReq;
    }

    private PostImageDownloadReq getPostImageDownloadReq(AlarmMsgInfo alarmMsgInfo) {
        PostImageDownloadReq postImageDownloadReq = new PostImageDownloadReq();
        postImageDownloadReq.setChannelId(alarmMsgInfo.getChannelId());
        postImageDownloadReq.setStreamType(2);
        postImageDownloadReq.setPlayType(PlayType.ALARM.getCode());
        postImageDownloadReq.setTime(alarmMsgInfo.getAlarmStartTime());
        postImageDownloadReq.setUploadId(String.valueOf(alarmMsgInfo.getId()));
        postImageDownloadReq.setUploadUrl(alarmProperties.getUploadUrl());
        return postImageDownloadReq;
    }


    private AlarmMsgErrorRel getAlarmMsgErrorRel(Long alarmMsgInfoId, AlarmFileType alarmFileType, String errorMsg, LocalDateTime nowTime) {
        AlarmMsgErrorRel alarmMsgErrorRel = new AlarmMsgErrorRel();
        alarmMsgErrorRel.setAlarmMsgId(alarmMsgInfoId);
        alarmMsgErrorRel.setErrorMsg(errorMsg);
        alarmMsgErrorRel.setAlarmFileType(alarmFileType.getCode());
        alarmMsgErrorRel.setCreateTime(nowTime);
        return alarmMsgErrorRel;
    }
}
