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
import com.runjian.stream.vo.response.GetGatewayRsp;
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

    @Override
    public PostVideoPlayRsp customLive(Long dispatchId, Long code, String protocol, Integer transferMode, String port, String ip, Integer streamMode, Boolean enableAudio, Boolean ssrcCheck, Integer recordState, Integer autoCloseState) {
        String streamId = PlayType.CUSTOM_LIVE.getMsg() + MarkConstant.MARK_SPLIT_SYMBOL + code;
        saveLiveStream(code, recordState, autoCloseState, null, dispatchId, streamId);
        StreamManageDto streamManageDto = new StreamManageDto(dispatchId, streamId, MsgType.STREAM_CUSTOM_LIVE_START, 15000L);
        streamManageDto.put(StandardName.STREAM_CUSTOM_CODE, code);
        streamManageDto.put(StandardName.STREAM_CUSTOM_PROTOCOL, protocol);
        streamManageDto.put(StandardName.STREAM_CUSTOM_TRANSFER_MODE, transferMode);
        streamManageDto.put(StandardName.STREAM_CUSTOM_PORT, port);
        if (Objects.nonNull(ip)){
            streamManageDto.put(StandardName.STREAM_CUSTOM_IP, ip);
        }
        streamManageDto.put(StandardName.STREAM_ENABLE_AUDIO, enableAudio);
        streamManageDto.put(StandardName.STREAM_SSRC_CHECK, ssrcCheck);
        streamManageDto.put(StandardName.STREAM_RECORD_STATE, recordState);
        streamManageDto.put(StandardName.STREAM_MODE, streamMode);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        if (commonResponse.isError()){
            streamMapper.deleteByStreamId(streamId);
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "流北向接口服务", "自定义直播播放失败", commonResponse.getMsg(), commonResponse.getData());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, commonResponse.getMsg());
        }
        streamMapper.updateStreamStateByStreamId(streamId, CommonEnum.ENABLE.getCode(), LocalDateTime.now());
        return JSONObject.parseObject(JSONObject.toJSONString(commonResponse.getData()), PostVideoPlayRsp.class);
    }


    @Override
    public PostVideoPlayRsp streamLivePlay(Long channelId, Integer streamMode, Boolean enableAudio, Boolean ssrcCheck, Integer recordState, Integer autoCloseState, Integer bitStreamId) {
        String streamId = PlayType.LIVE.getMsg() + MarkConstant.MARK_SPLIT_SYMBOL + channelId;
        StreamManageDto streamManageDto = getStreamManageDto(channelId, MsgType.STREAM_LIVE_PLAY_START, streamId, PlayType.LIVE.getCode(), streamMode, enableAudio, ssrcCheck, recordState, autoCloseState, bitStreamId);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        if (commonResponse.isError()){
            streamMapper.deleteByStreamId(streamId);
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "流北向接口服务", "直播播放失败", commonResponse.getMsg(), commonResponse.getData());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, commonResponse.getMsg());
        }
        streamMapper.updateStreamStateByStreamId(streamId, CommonEnum.ENABLE.getCode(), LocalDateTime.now());
        return JSONObject.parseObject(JSONObject.toJSONString(commonResponse.getData()), PostVideoPlayRsp.class);
    }

    private void saveLiveStream(Long channelId, Integer recordState, Integer autoCloseState, Long gatewayId, Long dispatchId, String streamId) {
        StreamInfo streamInfo;
        RLock lock = redissonClient.getLock(MarkConstant.REDIS_STREAM_LIVE_PLAY_LOCK + streamId);
        try{
            lock.lock();
            Optional<StreamInfo> streamInfoOp = streamMapper.selectByStreamId(streamId);
            if (streamInfoOp.isEmpty()) {
                saveStream(gatewayId, channelId, dispatchId, PlayType.LIVE.getCode(), recordState, autoCloseState, streamId);
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
    }

    /**
     * 获取网关与流媒体关系信息
     * @param channelId
     * @return
     */
    private StreamManageDto getStreamManageDto(Long channelId, MsgType msgType, String streamId, Integer playType, Integer streamMode, Boolean enableAudio, Boolean ssrcCheck, Integer recordState, Integer autoCloseState, Integer bitStreamId){
        CommonResponse<GetGatewayRsp> commonResponse = deviceControlApi.getGatewayIdByChannelId(channelId);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        GetGatewayRsp gatewayInfo = commonResponse.getData();
        if (Objects.isNull(gatewayInfo)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("设备%s未绑定网关", channelId));
        }
        Optional<GatewayDispatchInfo> dispatchIdOp = gatewayDispatchMapper.selectByGatewayId(gatewayInfo.getId());
        if (dispatchIdOp.isEmpty()) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("该网关模块%s未绑定流媒体模块，无法播放", gatewayInfo.getName()));
        }
        DispatchInfo dispatchInfo = dataBaseService.getOnlineDispatchInfo(dispatchIdOp.get().getDispatchId());

        if (msgType.equals(MsgType.STREAM_RECORD_PLAY_START)) {

            saveStream(gatewayInfo.getId(), channelId, dispatchInfo.getId(), playType, recordState, autoCloseState, streamId);
        } else {

            saveLiveStream(channelId, recordState, autoCloseState, gatewayInfo.getId(), dispatchInfo.getId(), streamId);
        }

        StreamManageDto streamManageDto = new StreamManageDto(dispatchInfo.getId(), streamId, msgType, 15000L);
        streamManageDto.put(StandardName.CHANNEL_ID, channelId);
        streamManageDto.put(StandardName.STREAM_ENABLE_AUDIO, enableAudio);
        streamManageDto.put(StandardName.STREAM_SSRC_CHECK, ssrcCheck);
        streamManageDto.put(StandardName.STREAM_RECORD_STATE, recordState);
        streamManageDto.put(StandardName.STREAM_MEDIA_URL, dispatchInfo.getUrl());
        streamManageDto.put(StandardName.STREAM_MODE, streamMode);
        streamManageDto.put(StandardName.GATEWAY_PROTOCOL, gatewayInfo.getProtocol());
        streamManageDto.put(StandardName.STREAM_BIT_STREAM_ID, bitStreamId);
        return streamManageDto;
    }

    @Override
    public PostVideoPlayRsp streamRecordPlay(Long channelId, Integer streamMode, Boolean enableAudio, Boolean ssrcCheck, Integer playType, Integer recordState, Integer autoCloseState, LocalDateTime startTime, LocalDateTime endTime, Integer bitStreamId){
        String streamId = PlayType.getMsgByCode(playType) + MarkConstant.MARK_SPLIT_SYMBOL + channelId + MarkConstant.MARK_SPLIT_SYMBOL + System.currentTimeMillis() + new Random().nextInt(100);
        StreamManageDto streamManageDto = getStreamManageDto(channelId, MsgType.STREAM_RECORD_PLAY_START, streamId, playType, streamMode, enableAudio, ssrcCheck, recordState, autoCloseState, bitStreamId);
        streamManageDto.put(StandardName.COM_START_TIME, DateUtils.DATE_TIME_FORMATTER.format(startTime));
        streamManageDto.put(StandardName.COM_END_TIME, DateUtils.DATE_TIME_FORMATTER.format(endTime));
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        if (commonResponse.isError()){
            streamMapper.deleteByStreamId(streamId);
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "流北向接口服务", "录像播放失败", commonResponse.getMsg(), commonResponse.getData());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, commonResponse.getMsg());
        }
        streamMapper.updateStreamStateByStreamId(streamId, CommonEnum.ENABLE.getCode(), LocalDateTime.now());
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
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_PLAY_STOP, 15000L));
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
        CommonResponse<?> response = parsingEngineApi.streamCustomEvent(new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_START, 15000L));
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
        CommonResponse<?> response = parsingEngineApi.streamCustomEvent(new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_STOP, 15000L));
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
        StreamManageDto streamManageDto = new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_SPEED, 15000L);
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
        StreamManageDto streamManageDto = new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_SEEK, 15000L);
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
        StreamManageDto streamManageDto = new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_PAUSE, 15000L);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

    @Override
    public void resumeRecord(String streamId) {
        StreamInfo streamInfo = dataBaseService.getStreamInfoByStreamId(streamId);
        if (streamInfo.getStreamState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "视频未正常播放，无法恢复视频");
        }
        StreamManageDto streamManageDto = new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_RESUME, 15000L);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

    @Override
    public JSONObject getStreamMediaInfo(String streamId) {
        StreamInfo streamInfo = dataBaseService.getStreamInfoByStreamId(streamId);
        if (streamInfo.getStreamState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "视频未正常播放，无法获取视频信息");
        }
        StreamManageDto streamManageDto = new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_MEDIA_INFO, 15000L);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        return JSONObject.parseObject(JSONObject.toJSONString(commonResponse.getData()));
    }



}
