package com.runjian.stream.service.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonEnum;
import com.runjian.stream.constant.PlayType;
import com.runjian.stream.dao.DispatchMapper;
import com.runjian.stream.dao.GatewayDispatchMapper;
import com.runjian.stream.dao.StreamMapper;
import com.runjian.stream.entity.DispatchInfo;
import com.runjian.stream.entity.GatewayDispatchInfo;
import com.runjian.stream.entity.StreamInfo;
import com.runjian.stream.feign.ParsingEngineApi;
import com.runjian.stream.service.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Miracle
 * @date 2023/2/6 11:17
 */
@Service
public class StreamServiceImpl implements StreamService {

    @Autowired
    private StreamMapper streamMapper;

    @Autowired
    private ParsingEngineApi parsingEngineApi;

    @Autowired
    private GatewayDispatchMapper gatewayDispatchMapper;

    private static final int CHANNEL_MAX_PLAY_NUM = 3;

    @Override
    public String getStreamId(Long gatewayId, Long channelId, Integer playType, Integer recordState, Integer autoCloseState) {
        if (playType.equals(PlayType.LIVE.getCode())) {
            String streamId = PlayType.getMsgByCode(playType) + "_" + channelId;
            Optional<StreamInfo> streamInfoOp = streamMapper.selectByStreamId(streamId);
            if (streamInfoOp.isEmpty()) {
                saveStream(gatewayId, channelId, playType, recordState, autoCloseState, streamId);
            } else {
                StreamInfo streamInfo = streamInfoOp.get();
                // 判断是否需要开启录像
                if (!streamInfo.getRecordState().equals(recordState) || recordState.equals(CommonEnum.ENABLE.getCode())) {
                    startRecord(streamId);
                }
            }
            return streamId;
        } else {
            List<StreamInfo> streamInfoList = streamMapper.selectByChannelId(channelId);
            if (streamInfoList.size() >= CHANNEL_MAX_PLAY_NUM) {
                throw new BusinessException(BusinessErrorEnums.VALID_REPETITIVE_OPERATION_ERROR, String.format("设备%s已达到并发播放数上限,请稍后重试", channelId));
            }
            String streamId = PlayType.getMsgByCode(playType) + "_" + channelId + "_" + System.currentTimeMillis() + new Random().nextInt(100);
            saveStream(gatewayId, channelId, playType, recordState, autoCloseState, streamId);
            return streamId;
        }
    }

    private StreamInfo saveStream(Long gatewayId, Long channelId, Integer playType, Integer recordState, Integer autoCloseState, String streamId) {
        Optional<GatewayDispatchInfo> dispatchIdOp = gatewayDispatchMapper.selectByGatewayId(gatewayId);
        if (dispatchIdOp.isEmpty()) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("该网关模块%s未绑定流媒体模块，无法播放", gatewayId));
        }
        LocalDateTime nowTime = LocalDateTime.now();
        StreamInfo streamInfo = new StreamInfo();
        streamInfo.setChannelId(channelId);
        streamInfo.setGatewayId(gatewayId);
        streamInfo.setDispatchId(dispatchIdOp.get().getDispatchId());
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
    public void receiveResult(String streamId, Boolean isSuccess) {
        Optional<StreamInfo> streamInfoOp = streamMapper.selectByStreamId(streamId);
        if (streamInfoOp.isEmpty()) {
            return;
        }
        if (isSuccess) {
            StreamInfo streamInfo = streamInfoOp.get();
            streamInfo.setStreamState(CommonEnum.ENABLE.getCode());
            streamInfo.setUpdateTime(LocalDateTime.now());
            streamMapper.updateStreamState(streamInfo);
        } else {
            streamMapper.deleteByStreamId(streamId);
        }
    }

    @Override
    public Boolean autoCloseHandle(String streamId) {
        Optional<StreamInfo> streamInfoOp = streamMapper.selectByStreamId(streamId);
        if (streamInfoOp.isEmpty() || streamInfoOp.get().getAutoCloseState().equals(CommonEnum.ENABLE.getCode())) {
            streamMapper.deleteByStreamId(streamId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopPlay(String streamId) {
        Optional<StreamInfo> streamInfoOp = streamMapper.selectByStreamId(streamId);
        if (streamInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("流%s不存在", streamId));
        }
        streamMapper.deleteByStreamId(streamId);
        CommonResponse<?> commonResponse = parsingEngineApi.channelStopPlay(streamId);
        if (commonResponse.getCode() != 0){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, commonResponse.getMsg());
        }
    }

    @Override
    public String startRecord(String streamId) {
        return null;
    }

    @Override
    public String stopRecord(String streamId) {
        return null;
    }
}
