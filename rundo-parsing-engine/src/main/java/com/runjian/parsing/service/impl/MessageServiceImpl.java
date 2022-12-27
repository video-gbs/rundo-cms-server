package com.runjian.parsing.service.impl;

import com.alibaba.fastjson2.JSON;
import com.runjian.parsing.constant.MqConstant;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.mq.config.RabbitMqProperties;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.service.GatewayService;
import com.runjian.parsing.service.MessageService;
import com.runjian.parsing.vo.GatewayMqDto;
import com.runjian.parsing.vo.request.GatewaySignInReq;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Value("${gateway.default.exchange-id}")
    private String exchangeId;

    @Value("gateway.public.queue-id-set")
    private String signInQueueId;

    @Override
    public void msgDispatch(Message message) {
        GatewayMqDto mqRequest = JSON.parseObject(new String(message.getBody()), GatewayMqDto.class);

        if (mqRequest.getMsgType().equals(MsgType.HEARTBEAT.getMsg())){
            Long gatewayId = gatewayService.heartbeat(mqRequest.getSerialNum(), mqRequest.getTime());
            GatewayMqDto mqResponse = (GatewayMqDto)GatewayMqDto.success();
            mqResponse.copyRequest(mqRequest);
            // 判断设备信息是否存在
            if (Objects.nonNull(gatewayId)){
                // 响应成功
                rabbitMqSender.sendMsgByRoutingKey(exchangeId, MqConstant.GATEWAY_PREFIX + MqConstant.GET_SET_PREFIX + gatewayId, UUID.randomUUID().toString().replace("-", ""),  mqResponse, true);
            }else {
                // 发送重新注册命令
                RabbitMqProperties.QueueData queueData = rabbitMqProperties.getQueueData(signInQueueId);
                rabbitMqSender.sendMsgByRoutingKey(queueData.getExchangeId(), queueData.getRoutingKey(), UUID.randomUUID().toString().replace("-", ""),  mqResponse, true);
            }
        }
    }
}
