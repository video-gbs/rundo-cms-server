package com.runjian.stream.service.north.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.*;
import com.runjian.stream.dao.GatewayDispatchMapper;
import com.runjian.stream.dao.StreamMapper;
import com.runjian.stream.entity.DispatchInfo;
import com.runjian.stream.entity.GatewayDispatchInfo;
import com.runjian.stream.entity.StreamInfo;
import com.runjian.stream.feign.ParsingEngineApi;
import com.runjian.stream.service.common.DataBaseService;
import com.runjian.stream.service.common.StreamBaseService;
import com.runjian.stream.service.north.StreamNorthService;
import com.runjian.stream.vo.StreamManageDto;
import com.runjian.stream.vo.response.PostApplyStreamRsp;
import lombok.extern.slf4j.Slf4j;
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
public class StreamNorthServiceImpl implements StreamNorthService {

    @Autowired
    private StreamMapper streamMapper;

    @Autowired
    private ParsingEngineApi parsingEngineApi;

    @Autowired
    private GatewayDispatchMapper gatewayDispatchMapper;

    @Autowired
    private DataBaseService dataBaseService;

    /**
     * 通道最大播放数
     */
//    private static final int CHANNEL_MAX_PLAY_NUM = 3;

    /**
     * 播放未响应超时时间
     */
    private static final long PREPARE_STREAM_OUT_TIME = 10L;


    @Override
    public PostApplyStreamRsp applyStreamId(Long gatewayId, Long channelId, Integer playType, Integer recordState, Integer autoCloseState) {
        Optional<GatewayDispatchInfo> dispatchIdOp = gatewayDispatchMapper.selectByGatewayId(gatewayId);
        if (dispatchIdOp.isEmpty()) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("该网关模块%s未绑定流媒体模块，无法播放", gatewayId));
        }
        DispatchInfo dispatchInfo = dataBaseService.getOnlineDispatchInfo(dispatchIdOp.get().getDispatchId());
        PostApplyStreamRsp res = new PostApplyStreamRsp();
        res.setDispatchUrl(dispatchInfo.getUrl());
        res.setRecordState(recordState);
        StreamInfo streamInfo;
        if (playType.equals(PlayType.LIVE.getCode())) {
            String streamId = PlayType.getMsgByCode(playType) + "_" + channelId;
            Optional<StreamInfo> streamInfoOp = streamMapper.selectByStreamId(streamId);
            if (streamInfoOp.isEmpty()) {
                streamInfo = saveStream(gatewayId, channelId, dispatchInfo.getId(), playType, recordState, autoCloseState, streamId);
                // 设置“准备中”状态的超时时间
                StreamBaseService.STREAM_OUT_TIME_ARRAY.addOrUpdateTime(streamInfo.getStreamId(), PREPARE_STREAM_OUT_TIME);
            } else {
                streamInfo = streamInfoOp.get();
                if (streamInfo.getStreamState().equals(CommonEnum.DISABLE.getCode())){
                    // 设置“准备中”状态的超时时间
                    StreamBaseService.STREAM_OUT_TIME_ARRAY.addOrUpdateTime(streamInfo.getStreamId(), PREPARE_STREAM_OUT_TIME);
                }
                // 判断是否需要开启录像
                if (!streamInfo.getRecordState().equals(recordState) || recordState.equals(CommonEnum.ENABLE.getCode())) {
                    startRecord(streamId);
                }
            }
        } else {
//            List<StreamInfo> streamInfoList = streamMapper.selectByChannelId(channelId);
//            if (streamInfoList.size() >= CHANNEL_MAX_PLAY_NUM) {
//                throw new BusinessException(BusinessErrorEnums.VALID_REPETITIVE_OPERATION_ERROR, String.format("设备%s已达到并发播放数上限,请稍后重试", channelId));
//            }
            String streamId = PlayType.getMsgByCode(playType) + "_" + channelId + "_" + System.currentTimeMillis() + new Random().nextInt(100);
            streamInfo = saveStream(gatewayId, channelId, dispatchInfo.getId(), playType, recordState, autoCloseState, streamId);
            // 设置“准备中”状态的超时时间
            StreamBaseService.STREAM_OUT_TIME_ARRAY.addOrUpdateTime(streamInfo.getStreamId(), PREPARE_STREAM_OUT_TIME);
        }
        res.setStreamId(streamInfo.getStreamId());
        return res;
    }

    private StreamInfo saveStream(Long gatewayId, Long channelId, Long dispatchId, Integer playType, Integer recordState, Integer autoCloseState, String streamId) {
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

        if (Boolean.getBoolean(commonResponse.getData().toString())){
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
        if (Boolean.getBoolean(response.getData().toString())){
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
        if (Boolean.getBoolean(response.getData().toString())){
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
        if (streamInfo.getRecordState().equals(CommonEnum.DISABLE.getCode())){
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
        if (streamInfo.getRecordState().equals(CommonEnum.DISABLE.getCode())){
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
        if (streamInfo.getRecordState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "视频未正常播放，无法暂停视频");
        }
        StreamManageDto streamManageDto = new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_PAUSE, 10L);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

    @Override
    public void resumeRecord(String streamId) {
        StreamInfo streamInfo = dataBaseService.getStreamInfoByStreamId(streamId);
        if (streamInfo.getRecordState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "视频未正常播放，无法恢复视频");
        }
        StreamManageDto streamManageDto = new StreamManageDto(streamInfo.getDispatchId(), streamId, MsgType.STREAM_RECORD_RESUME, 10L);
        CommonResponse<?> commonResponse = parsingEngineApi.streamCustomEvent(streamManageDto);
        commonResponse.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
    }

}
