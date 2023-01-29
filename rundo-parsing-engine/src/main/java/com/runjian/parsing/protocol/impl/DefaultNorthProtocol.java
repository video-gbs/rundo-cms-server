package com.runjian.parsing.protocol.impl;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.IdType;
import com.runjian.parsing.constant.MqConstant;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.entity.ChannelInfo;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.protocol.NorthProtocol;
import com.runjian.parsing.service.DataBaseService;
import com.runjian.parsing.service.TaskService;
import com.runjian.parsing.vo.CommonMqDto;
import com.runjian.parsing.vo.dto.GatewayConvertDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 默认协议
 * @author Miracle
 * @date 2023/1/17 14:14
 */
@Service
public class DefaultNorthProtocol implements NorthProtocol {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    private DataBaseService dataBaseService;

    @Value("${gateway.default.exchange-id}")
    private String gatewayExchangeId;

    @Override
    public String getProtocolName() {
        return DEFAULT_PROTOCOL;
    }

    @Override
    public void deviceSync(Long deviceId, DeferredResult<CommonResponse<?>> response) {
        customEvent(deviceId, IdType.DEVICE, MsgType.CHANNEL_RECORD.getMsg(), null, response);
    }

    @Override
    public void deviceAdd(Long gatewayId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        customEvent(gatewayId, IdType.GATEWAY, MsgType.DEVICE_ADD.getMsg(), dataMap, response);
    }

    @Override
    public  void deviceDelete(Long deviceId, DeferredResult<CommonResponse<?>> response) {
        customEvent(deviceId, IdType.DEVICE, MsgType.CHANNEL_RECORD.getMsg(), null, response);
    }

    @Override
    public void channelSync(Long deviceId, DeferredResult<CommonResponse<?>> response) {
        customEvent(deviceId, IdType.DEVICE, MsgType.CHANNEL_SYNC.getMsg(), null, response);
    }

    @Override
    public void channelPtzControl(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        customEvent(channelId, IdType.CHANNEL, MsgType.CHANNEL_PTZ_CONTROL.getMsg(), null, response);
    }

    @Override
    public void channelPlay(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        customEvent(channelId, IdType.CHANNEL, MsgType.CHANNEL_PLAY.getMsg(), dataMap, response);
    }

    @Override
    public void channelRecord(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        customEvent(channelId, IdType.CHANNEL, MsgType.CHANNEL_RECORD.getMsg(), dataMap, response);
    }

    @Override
    public void channelPlayback(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        customEvent(channelId, IdType.CHANNEL, MsgType.CHANNEL_PLAYBACK.getMsg(), dataMap, response);
    }

    @Override
    public void customEvent(Long mainId, IdType idType, String msgType, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response) {
        GatewayConvertDto gatewayConvertDto = new GatewayConvertDto();
        gatewayConvertDto.setDataMap(dataMap);
        Long deviceId = null;
        Long channelId = null;
        switch (idType){
            case CHANNEL:
                ChannelInfo channelInfo = dataBaseService.getChannelInfo(mainId);
                gatewayConvertDto.setChannelId(channelInfo.getOriginId());
                channelId = mainId;
                mainId = channelInfo.getDeviceId();
            case DEVICE:
                DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(mainId);
                gatewayConvertDto.setDeviceId(deviceInfo.getOriginId());
                deviceId = mainId;
                mainId = deviceInfo.getGatewayId();
            case GATEWAY:
                GatewayInfo gatewayInfo = dataBaseService.getGatewayInfo(mainId);
                sendMsgToGateway(gatewayInfo.getSerialNum(), gatewayInfo.getId(), deviceId, channelId, msgType, gatewayConvertDto, response);
                break;
        }
    }

    /**
     * 发送消息
     * @param serialNum 网关序列号
     * @param gatewayId 网关id
     * @param deviceId 设备id
     * @param channelId 通道id
     * @param response 消息返回体
     * @param msgType 消息类型
     * @param data 数据
     */
    protected void sendMsgToGateway(String serialNum,Long gatewayId, Long deviceId, Long channelId, String msgType, Object data, DeferredResult<CommonResponse<?>> response) {
        String mqId = UUID.randomUUID().toString().replace("-", "");
        Long taskId = taskService.createAsyncTask(gatewayId, deviceId, channelId, null, mqId, msgType, response);
        String mqKey = MqConstant.GATEWAY_PREFIX + MqConstant.GET_SET_PREFIX + gatewayId;
        CommonMqDto<Object> request = new CommonMqDto<>();
        request.setTime(LocalDateTime.now());
        request.setSerialNum(serialNum);
        request.setMsgId(taskId.toString());
        request.setMsgType(msgType);
        request.setData(data);
        rabbitMqSender.sendMsgByRoutingKey(gatewayExchangeId, mqKey, mqId, request, true);
    }
}
