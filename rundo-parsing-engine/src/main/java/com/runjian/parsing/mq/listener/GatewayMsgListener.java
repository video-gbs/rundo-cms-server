package com.runjian.parsing.mq.listener;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.parsing.constant.IdType;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.dao.GatewayMapper;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.service.common.ProtocolService;
import com.runjian.parsing.vo.CommonMqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class GatewayMsgListener implements ChannelAwareMessageListener {

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private GatewayMapper gatewayMapper;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    private MqDefaultProperties mqDefaultProperties;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            CommonMqDto<?> mqRequest = JSON.parseObject(new String(message.getBody()), CommonMqDto.class);
            Optional<GatewayInfo> gatewayInfoOp = gatewayMapper.selectBySerialNum(mqRequest.getSerialNum());
            if (gatewayInfoOp.isEmpty()) {
                CommonMqDto<?> mqResponse = CommonMqDto.createByCommonResponse(CommonResponse.success());
                mqResponse.copyRequest(mqRequest);
                // 发送重新注册命令
                mqResponse.setMsgType(MsgType.GATEWAY_RE_SIGN_IN.getMsg());
                String mqId = UUID.randomUUID().toString().replace("-", "");
                rabbitMqSender.sendMsgByRoutingKey(mqDefaultProperties.getPublicSetQueueData().getExchangeId(), mqDefaultProperties.getPublicSetQueueData().getRoutingKey(), mqId, mqResponse, true);
                return;
            }
            GatewayInfo gatewayInfo = gatewayInfoOp.get();

            // 异常消息记录
            if (mqRequest.getCode() != BusinessErrorEnums.SUCCESS.getErrCode()){
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "MQ流媒体消息处理服务", "流媒体异常消息记录", mqRequest.getMsgType(), mqRequest.getMsg());
            }


            if (!StringUtils.isNumber(mqRequest.getMsgId())){
                // 网关主动推送消息
                if (mqRequest.getMsgType().equals(MsgType.DEVICE_SIGN_IN.getMsg())) {
                    protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).deviceSignIn(gatewayInfo.getId(), mqRequest.getData());
                } else if (mqRequest.getMsgType().equals(MsgType.DEVICE_TOTAL_SYNC.getMsg())){
                    protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).deviceBatchSignIn(gatewayInfo.getId(), mqRequest.getData());
                } else {
                    protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).commonEvent(gatewayInfo.getId(), mqRequest.getMsgId(), mqRequest.getMsgType(), mqRequest.getData());
                }
            } else{
                // 上层消息返回
                if (mqRequest.getMsgType().equals(MsgType.DEVICE_SYNC.getMsg())) {
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
                } else if (mqRequest.getMsgType().equals(MsgType.CHANNEL_RECORD_INFO.getMsg())) {
                    protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).channelRecord(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
                } else if (mqRequest.getMsgType().equals(MsgType.CHANNEL_PLAYBACK.getMsg())) {
                    protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).channelPlayback(Long.parseLong(mqRequest.getMsgId()), mqRequest.getData());
                } else {
                    protocolService.getSouthProtocol(gatewayInfo.getId(), IdType.GATEWAY).commonEvent(gatewayInfo.getId(), mqRequest.getMsgId(), mqRequest.getMsgType(), mqRequest.getData());
                }
            }
        } catch (Exception ex) {
            if (ex instanceof BusinessException){
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "MQ网关消息处理服务", "处理失败", new String(message.getBody()), ex.getMessage());
            }else {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "MQ网关消息处理服务", "处理失败", new String(message.getBody()), ex);
            }
        } finally {
            channel.basicAck(deliveryTag, true);
        }
    }
}
