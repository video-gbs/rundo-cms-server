package com.runjian.parsing.service.north.impl;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.MsgType;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.constant.MqConstant;
import com.runjian.parsing.entity.ChannelInfo;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.mq.config.RabbitMqProperties;
import com.runjian.parsing.mq.listener.MqDefaultProperties;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.service.common.StreamTaskService;
import com.runjian.parsing.service.north.StreamNorthService;
import com.runjian.parsing.vo.dto.StreamConvertDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;

/**
 * @author Miracle
 * @date 2023/2/10 10:26
 */
@Service
@RequiredArgsConstructor
public class StreamNorthServiceImpl implements StreamNorthService {

    private final StreamTaskService streamTaskService;

    private final DataBaseService dataBaseService;

    private final MqDefaultProperties mqDefaultProperties;

    private final RabbitMqProperties rabbitMqProperties;

    /**
     * 通用消息处理
     * @param dispatchId 调度服务id
     * @param streamId 流id
     * @param mapData 数据
     * @param msgType 消息类型
     * @param response 消息返回体
     */
    @Override
    public void customEvent(Long dispatchId, String streamId, Map<String, Object> mapData, MsgType msgType, DeferredResult<CommonResponse<?>> response) {
        StreamConvertDto streamConvertDto = new StreamConvertDto();
        streamConvertDto.setStreamId(streamId);
        Object channelIdOb = mapData.get(StandardName.CHANNEL_ID);
        if (Objects.nonNull(channelIdOb)){
            Long channelId = Long.parseLong(channelIdOb.toString());
            ChannelInfo channelInfo = dataBaseService.getChannelInfo(channelId);
            DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(channelInfo.getDeviceId());
            mapData.put(StandardName.CHANNEL_ID, channelInfo.getOriginId());
            mapData.put(StandardName.DEVICE_ID, deviceInfo.getOriginId());
            mapData.put(StandardName.GATEWAY_MQ, MqConstant.GATEWAY_PREFIX + MqConstant.GET_SET_PREFIX + deviceInfo.getGatewayId());
            mapData.put(StandardName.GATEWAY_EXCHANGE_NAME, rabbitMqProperties.getExchangeData(mqDefaultProperties.getGatewayExchangeId()).getExchange().getName());
        }
        streamConvertDto.setDataMap(mapData);
        streamTaskService.sendMsgToGateway(dispatchId,  streamId, msgType.getMsg(), streamConvertDto, response);
    }
}
