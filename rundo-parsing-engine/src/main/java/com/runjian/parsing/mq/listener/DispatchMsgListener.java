package com.runjian.parsing.mq.listener;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.LogTemplate;
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
        try {
            CommonMqDto<?> mqRequest = JSON.parseObject(new String(message.getBody()), CommonMqDto.class);
            Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectBySerialNum(mqRequest.getSerialNum());
            if (gatewayInfoOp.isEmpty()) {
                throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("网关不存在，网关序列号：%s", mqRequest.getSerialNum()));
            }
            GatewayInfo gatewayInfo = gatewayInfoOp.get();
            if (!StringUtils.isNumber(mqRequest.getMsgId())){
                protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).commonEvent(gatewayInfo.getId(), mqRequest.getMsgId(), mqRequest.getMsgType(), mqRequest.getData());
            }
            if (mqRequest.getMsgType().equals(MsgType.DEVICE_SIGN_IN.getMsg())) {
                protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).deviceSignIn(gatewayInfo.getId(), mqRequest.getData());
            } else if (mqRequest.getMsgType().equals(MsgType.DEVICE_SYNC.getMsg())) {
                protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).deviceSync(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
            } else if (mqRequest.getMsgType().equals(MsgType.DEVICE_ADD.getMsg())) {
                protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).deviceAdd(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
            } else if (mqRequest.getMsgType().equals(MsgType.DEVICE_DELETE.getMsg())) {
                protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).deviceDelete(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
            } else if (mqRequest.getMsgType().equals(MsgType.CHANNEL_SYNC.getMsg())) {
                protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).channelSync(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
            } else if (mqRequest.getMsgType().equals(MsgType.CHANNEL_PTZ_CONTROL.getMsg())) {
                protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).channelPtzControl(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
            } else if (mqRequest.getMsgType().equals(MsgType.CHANNEL_PLAY.getMsg())) {
                protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).channelPlay(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
            } else if (mqRequest.getMsgType().equals(MsgType.CHANNEL_RECORD.getMsg())) {
                protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).channelRecord(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
            } else if (mqRequest.getMsgType().equals(MsgType.CHANNEL_PLAYBACK.getMsg())) {
                protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).channelPlayback(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
            } else {
                protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).commonEvent(gatewayInfo.getId(), mqRequest.getMsgId(), mqRequest.getMsgType(), mqRequest.getData());
            }
        } catch (Exception ex) {
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "MQ网关消息处理服务", "处理失败", new String(message.getBody()), ex);
        } finally {
            channel.basicAck(deliveryTag, true);
        }
    }
}
