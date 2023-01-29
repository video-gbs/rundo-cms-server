package com.runjian.parsing.mq.listener;

import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.parsing.constant.IdType;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.dao.GatewayMapper;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.service.ProtocolService;
import com.runjian.parsing.vo.CommonMqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
public class DispatchMsgListener implements ChannelAwareMessageListener {

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private GatewayMapper gatewayMapper;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        CommonMqDto<?> mqRequest = JSON.parseObject(new String(message.getBody()), CommonMqDto.class);
        Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectBySerialNum(mqRequest.getSerialNum());
        if (gatewayInfoOp.isEmpty()){
            channel.basicReject(deliveryTag, false);
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("网关不存在，网关序列号：%s", mqRequest.getSerialNum()));
        }
        GatewayInfo gatewayInfo = gatewayInfoOp.get();
        if (mqRequest.getMsgType().equals(MsgType.DEVICE_SIGN_IN.getMsg())){
            protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).deviceSignIn(gatewayInfo.getId(), mqRequest.getData());
        } else if (mqRequest.getMsgType().equals(MsgType.DEVICE_SYNC.getMsg())) {
            protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).deviceSync(gatewayInfo.getId(), mqRequest.getData());
        } else if (mqRequest.getMsgType().equals(MsgType.DEVICE_ADD.getMsg())) {
            protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).deviceAdd(gatewayInfo.getId(), mqRequest.getData());
        } else if (mqRequest.getMsgType().equals(MsgType.DEVICE_DELETE.getMsg())) {
            protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).deviceDelete(gatewayInfo.getId(), mqRequest.getData());
        } else if (mqRequest.getMsgType().equals(MsgType.CHANNEL_SYNC.getMsg())) {
            protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).channelSync(gatewayInfo.getId(), mqRequest.getData());
        } else if (mqRequest.getMsgType().equals(MsgType.CHANNEL_PLAY.getMsg())) {
            protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).channelPlay(gatewayInfo.getId(), mqRequest.getData());
        } else if (mqRequest.getMsgType().equals(MsgType.CHANNEL_RECORD.getMsg())) {
            protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).channelRecord(gatewayInfo.getId(), mqRequest.getData());
        } else if (mqRequest.getMsgType().equals(MsgType.CHANNEL_PLAYBACK.getMsg())) {
            protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).channelPlayback(gatewayInfo.getId(), mqRequest.getData());
        } else {
            protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).customEvent(gatewayInfo.getId(), mqRequest.getData());
        }
        channel.basicAck(deliveryTag, true);
    }
}
