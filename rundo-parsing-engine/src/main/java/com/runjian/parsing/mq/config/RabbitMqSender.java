package com.runjian.parsing.mq.config;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.LogTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * @author Miracle
 * @date 2022/5/23 18:47
 */
@Slf4j
@Component
@ConditionalOnBean(RabbitMqConfig.class)
@RequiredArgsConstructor
public class RabbitMqSender {

    private final RabbitTemplate rabbitTemplate;

    private final RabbitMqProperties rabbitMqProperties;

    /**
     * @param queueId 队列名称
     * @param msgId 消息ID
     * @param msg 消息体
     * @param convertStrJson 是否转化为字符串JSON
     * @throws BusinessException
     */
    public void sendMsg(String queueId, String msgId, Object msg, boolean convertStrJson) throws BusinessException {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(msgId);
        RabbitMqProperties.QueueData queueData = rabbitMqProperties.getQueueData(queueId);
        RabbitMqProperties.ExchangeData exchangeData = rabbitMqProperties.getExchangeData(queueData.getExchangeId());
        if (convertStrJson){
            try {
                rabbitTemplate.convertAndSend(exchangeData.getName(), queueData.getRoutingKey(), JSONObject.toJSONString(msg), correlationData);
            } catch (Exception e) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "MQ发送消息服务", "消息发送失败，消息格式化异常", msg, e);
            }
        }else {
            rabbitTemplate.convertAndSend(exchangeData.getName(), queueData.getRoutingKey(), msg, correlationData);
        }
    }

    public void sendMsgByRoutingKey(String exchangeId, String routingKey, String msgId, Object msg, boolean convertStrJson) throws BusinessException {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(msgId);
        RabbitMqProperties.ExchangeData exchangeData = rabbitMqProperties.getExchangeData(exchangeId);
        if (convertStrJson){
            try {
                rabbitTemplate.convertAndSend(exchangeData.getName(), routingKey, JSONObject.toJSONString(msg), correlationData);
            } catch (Exception e) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "MQ发送消息服务", "消息发送失败，消息格式化异常", msg, e);
            }
        }else {
            rabbitTemplate.convertAndSend(exchangeData.getName(), routingKey, msg, correlationData);
        }
    }

}
