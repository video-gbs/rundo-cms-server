package com.runjian.stream.service.north.impl;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.*;
import com.runjian.common.utils.DateUtils;
import com.runjian.stream.dao.GatewayDispatchMapper;
import com.runjian.stream.dao.StreamMapper;
import com.runjian.stream.entity.DispatchInfo;
import com.runjian.stream.entity.GatewayDispatchInfo;
import com.runjian.stream.entity.StreamInfo;
import com.runjian.stream.feign.DeviceControlApi;
import com.runjian.stream.feign.ParsingEngineApi;
import com.runjian.stream.service.common.DataBaseService;
import com.runjian.stream.service.common.StreamBaseService;
import com.runjian.stream.service.north.StreamNorthService;
import com.runjian.stream.vo.StreamManageDto;
import com.runjian.stream.vo.response.PostApplyStreamRsp;
import com.runjian.stream.vo.response.PostVideoPlayRsp;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Miracle
 * @date 2023/2/6 11:17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StreamNorthServiceImpl implements StreamNorthService {

    private final StreamMapper streamMapper;

    private final ParsingEngineApi parsingEngineApi;

    private final GatewayDispatchMapper gatewayDispatchMapper;

    private final DataBaseService dataBaseService;

    private final RedissonClient redissonClient;

    private final DeviceControlApi deviceControlApi;

    /**
     * 通道最大播放数
     */
//    private static final int CHANNEL_MAX_PLAY_NUM = 3;


    /**
     * 播放未响应超时时间
     */
    private static final long PREPARE_STREAM_OUT_TIME = 10L;


    /**
     * 直播播放
     * @param channelId 通道id
     * @param enableAudio 是否播放音频
     * @param ssrcCheck 是否使用ssrc
     * @param recordState 是否开启录播
     * @param autoCloseState 是否无人观看
     * @return
     */
    @Override
    public PostVideoPlayRsp streamLivePlay(Long channelId, Boolean enableAudio, Boolean ssrcCheck, Integer recordState, Integer autoCloseState) {
        GatewayDispatchInfo gatewayDispatchInfo = getGatewayDispatchInfoByChannelId(channelId);
        DispatchInfo dispatchInfo = dataBaseService.getOnlineDispatchInfo(gatewayDispatchInfo.getDispatchId());
        StreamInfo streamInfo;

            String streamId = PlayType.LIVE.getMsg() + "_" + channelId;
            RLock lock = redissonClient.getLock(MarkConstant.REDIS_STREAM_LIVE_PLAY_LOCK + streamId);
            try{
                lock.lock();
                Optional<StreamInfo> streamInfoOp = streamMapper.selectByStreamId(streamId);
                if (streamInfoOp.isEmpty()) {
                    streamInfo = saveStream(gatewayDispatchInfo.getGatewayId(), channelId, dispatchInfo.getId(), PlayType.LIVE.getCode(), recordState, autoCloseState, streamId);
                } else {
                    streamInfo = streamInfoOp.get();
                    // 判断是否需要开启录像
                    if (!streamInfo.getRecordState().equals(recordState) || recordState.equals(CommonEnum.ENABLE.getCode())) {
                        startRecord(streamId);
                    }
                }
            }finally {
                lock.unlock();
            }

        StreamManageDto streamManageDto = new StreamManageDto(dispatchInfo.getId(), streamInfo.getStreamId(), MsgType.STREAM_LIVE_PLAY_START, 15L);
        streamManageDto.put(StandardName.STREAM_ENABLE_AUDIO, enableAudio);
        streamManageDto.put(StandardName.STREAM_SSRC_CHECK, ssrcCheck);
        streamManageDto.put(StandardName.STREAM_RECORD_STATE, recordState);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);

        return JSONObject.parseObject(JSONObject.toJSONString(commonResponse.getData()), PostVideoPlayRsp.class);
    }

    private GatewayDispatchInfo getGatewayDispatchInfoByChannelId(Long channelId){
        CommonResponse<Long> commonResponse = deviceControlApi.getGatewayIdByChannelId(channelId);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        Long gatewayId = commonResponse.getData();
        if (Objects.isNull(gatewayId)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("设备%s未绑定网关", channelId));
        }
        Optional<GatewayDispatchInfo> dispatchIdOp = gatewayDispatchMapper.selectByGatewayId(gatewayId);
        if (dispatchIdOp.isEmpty()) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("该网关模块%s未绑定流媒体模块，无法播放", gatewayId));
        }
        return dispatchIdOp.get();
    }

    /**
     * 录播播放
     * @param channelId
     * @param enableAudio
     * @param ssrcCheck
     * @param playType
     * @param recordState
     * @param autoCloseState
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public PostVideoPlayRsp streamRecordPlay(Long channelId, Boolean enableAudio, Boolean ssrcCheck, Integer playType, Integer recordState, Integer autoCloseState, LocalDateTime startTime, LocalDateTime endTime){
        //            List<StreamInfo> streamInfoList = streamMapper.selectByChannelId(channelId);
//            if (streamInfoList.size() >= CHANNEL_MAX_PLAY_NUM) {
//                throw new BusinessException(BusinessErrorEnums.VALID_REPETITIVE_OPERATION_ERROR, String.format("设备%s已达到并发播放数上限,请稍后重试", channelId));
//            }
        GatewayDispatchInfo gatewayDispatchInfo = getGatewayDispatchInfoByChannelId(channelId);
        DispatchInfo dispatchInfo = dataBaseService.getOnlineDispatchInfo(gatewayDispatchInfo.getDispatchId());
        String streamId = PlayType.getMsgByCode(playType) + "_" + channelId + "_" + System.currentTimeMillis() + new Random().nextInt(100);
        StreamInfo streamInfo = saveStream(gatewayDispatchInfo.getGatewayId(), channelId, dispatchInfo.getId(), playType, recordState, autoCloseState, streamId);

        StreamManageDto streamManageDto = new StreamManageDto(dispatchInfo.getId(), streamInfo.getStreamId(), MsgType.STREAM_RECORD_PLAY_START, 15L);
        streamManageDto.put(StandardName.STREAM_ENABLE_AUDIO, enableAudio);
        streamManageDto.put(StandardName.STREAM_SSRC_CHECK, ssrcCheck);
        streamManageDto.put(StandardName.STREAM_RECORD_STATE, recordState);
        streamManageDto.put(StandardName.COM_START_TIME, DateUtils.DATE_TIME_FORMATTER.format(startTime));
        streamManageDto.put(StandardName.COM_END_TIME, DateUtils.DATE_TIME_FORMATTER.format(endTime));
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);

        return JSONObject.parseObject(JSONObject.toJSONString(commonResponse.getData()), PostVideoPlayRsp.class);
    }

    private  StreamInfo saveStream(Long gatewayId, Long channelId, Long dispatchId, Integer playType, Integer recordState, Integer autoCloseState, String streamId) {
        LocalDateTime nowTime = LocalDateTime.now();
        StreamInfo streamInfo = new StreamInfo();
        streamInfo.setChannelId(channelId);
        streamInfo.setGatewayId(gatewayId);
        streamInfo.setDispatchId(dispatchId);
        streamInfo.setRecordState(recordState);
        streamInfo.setAutoCloseState(autoCloseState);
        streamInfo.setStreamState(CommonEnum.DISABLE.getCode());
        streamInfo.setPlayType(playType);
        streamInfo.setStreamId(streamId);
        streamInfo.setUpdateTime(nowTime);
        streamInfo.setCreateTime(nowTime);
        streamMapper.save(streamInfo);
        return streamInfo;
    }



    @Override
    public void stopPlay(String streamId) {
        Optional<StreamInfo> streamInfoOP = streamMapper.selectByStreamId(streamId);
        if (streamInfoOP.isEmpty()){
            return;
        }
        StreamInfo streamInfo = streamInfoOP.get();
        streamInfo.setAutoCloseState(CommonEnum.ENABLE.getCode());
        streamInfo.setRecordState(CommonEnum.DISABLE.getCode());
        streamInfo.setUpdateTime(LocalDateTime.now());
        streamMapper.updateRecordAndAutoCloseState(streamInfo);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_PLAY_STOP, 10L));
        if (commonResponse.isError()){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "流北向服务", "流媒体交互失败", streamId, commonResponse.getMsg());
            streamMapper.deleteByStreamId(streamId);
            return;
        }
        if ((Boolean) commonResponse.getData()){
            streamMapper.deleteByStreamId(streamId);
        }
    }

    @Override
    public Boolean startRecord(String streamId) {
        StreamInfo streamInfo = dataBaseService.getStreamInfoByStreamId(streamId);
        if (streamInfo.getRecordState().equals(CommonEnum.ENABLE.getCode())){
            return true;
        }
        CommonResponse<?> response = parsingEngineApi.streamCustomEvent(new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_START, 10L));
        response.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        if ((Boolean) response.getData()){
            streamInfo.setRecordState(CommonEnum.ENABLE.getCode());
            streamInfo.setUpdateTime(LocalDateTime.now());
            streamMapper.updateRecordState(streamInfo);
        }
        return true;
    }

    @Override
    public Boolean stopRecord(String streamId) {
        StreamInfo streamInfo = dataBaseService.getStreamInfoByStreamId(streamId);
        if (streamInfo.getRecordState().equals(CommonEnum.DISABLE.getCode())){
            return true;
        }
        CommonResponse<?> response = parsingEngineApi.streamCustomEvent(new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_STOP, 10L));
        response.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        if ((Boolean) response.getData()){
            streamInfo.setRecordState(CommonEnum.DISABLE.getCode());
            streamInfo.setUpdateTime(LocalDateTime.now());
            streamMapper.updateRecordState(streamInfo);
        }
        return true;
    }

    @Override
    public List<StreamInfo> getRecordStates(List<String> streamIds, Integer recordState, Integer streamState) {
        if (streamIds.size() == 0){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "流id不能为空");
        }
        return streamMapper.selectByStreamIdsAndRecordStateAndStreamState(streamIds, recordState, streamState);
    }

    @Override
    public void speedRecord(String streamId, Float speed) {
        StreamInfo streamInfo = dataBaseService.getStreamInfoByStreamId(streamId);
        if (streamInfo.getStreamState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "视频未正常播放，无法调整速度");
        }
        StreamManageDto streamManageDto = new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_SPEED, 10L);
        streamManageDto.put(StandardName.RECORD_SPEED, speed);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

    @Override
    public void seekRecord(String streamId, LocalDateTime currentTime, LocalDateTime targetTime) {
        StreamInfo streamInfo = dataBaseService.getStreamInfoByStreamId(streamId);
        if (streamInfo.getStreamState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "视频未正常播放，无法拖动进度条");
        }
        StreamManageDto streamManageDto = new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_SEEK, 10L);
        streamManageDto.put(StandardName.RECORD_CURRENT_TIME, currentTime);
        streamManageDto.put(StandardName.RECORD_TARGET_TIME, targetTime);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

    @Override
    public void pauseRecord(String streamId) {
        StreamInfo streamInfo = dataBaseService.getStreamInfoByStreamId(streamId);
        if (streamInfo.getStreamState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "视频未正常播放，无法暂停视频");
        }
        StreamManageDto streamManageDto = new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_PAUSE, 10L);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

    @Override
    public void resumeRecord(String streamId) {
        StreamInfo streamInfo = dataBaseService.getStreamInfoByStreamId(streamId);
        if (streamInfo.getStreamState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "视频未正常播放，无法恢复视频");
        }
        StreamManageDto streamManageDto = new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_RESUME, 10L);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

    @Override
    public JSONObject getStreamMediaInfo(String streamId) {
        StreamInfo streamInfo = dataBaseService.getStreamInfoByStreamId(streamId);
        if (streamInfo.getStreamState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "视频未正常播放，无法获取视频信息");
        }
        StreamManageDto streamManageDto = new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_MEDIA_INFO, 10L);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        return JSONObject.parseObject(JSONObject.toJSONString(commonResponse.getData()));
    }

}
