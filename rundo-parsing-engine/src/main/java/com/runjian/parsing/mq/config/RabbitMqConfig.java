package com.runjian.parsing.mq.config;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.LogTemplate;
import com.runjian.parsing.mq.listener.MqListenerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2022/5/23 18:21
 */

@Slf4j
@Configuration
@EnableConfigurationProperties(RabbitMqProperties.class)
@ConditionalOnProperty(prefix = "mq", value = "enabled", havingValue = "true")
public class RabbitMqConfig {


    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqProperties rabbitMqProperties;


    @PostConstruct
    private void initRabbitTemplate(){
        /**
         * 消息发送到交换机Exchange失败时, 回调
         * todo 修改为标准日志
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "MQ消息发送确认回调服务", "消息发送到Exchange成功", correlationData);
                return;
            }
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "MQ消息发送确认回调服务", "消息发送到Exchange失败", correlationData, cause);
        });

        /**
         * 消息从交换机Exchange-->队列Queue失败时, 优先回调
         */
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "MQ消息发送确认回调服务", "消息发送到Queue失败", message, "routingKey:" + routingKey + " exchange:" + exchange + " replyCode:" + replyCode + " replyText:" + replyText );
        });

        createExchangeQueue();

    }

    public void addQueue(Queue queue, Binding binding){
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);
    }

    public Queue addQueue(String exchangeId, String queueKey, Integer ttl){
        Map<String, Object> map = new HashMap<>();
        // 队列中的消息未被消费则15秒后过期
        map.put("x-message-ttl", ttl);
        Queue queue = new Queue(queueKey, true, false, false, map);
        AbstractExchange exchange = rabbitMqProperties.getExchangeDataMap().get(exchangeId).getExchange();
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(queueKey).noargs();
        addQueue(queue, binding);
        return queue;
    }

    public void deleteQueue(String queueName){
        rabbitAdmin.deleteQueue(queueName);
    }

    /**
     * 创建exchange、queue、binding
     */
    public void createExchangeQueue() throws BusinessException {
        List<RabbitMqProperties.ExchangeData> exchangeDataList = rabbitMqProperties.getExchangeDataList();
        if (exchangeDataList.size() > 0){
            Map<String, RabbitMqProperties.ExchangeData> exchangeDataMap = new HashMap<>(exchangeDataList.size());
            // 创建交换器
            for (RabbitMqProperties.ExchangeData exchangeData : exchangeDataList){
                AbstractExchange exchange;
                switch (ExchangeType.getTypeByType(exchangeData.getType())){
                    case TOPIC:
                        exchange = new TopicExchange(exchangeData.getName());
                        break;
                    case DIRECT:
                        exchange = new DirectExchange(exchangeData.getName());
                        break;
                    case FANOUT:
                        exchange = new FanoutExchange(exchangeData.getName());
                        break;
                    case HEADERS:
                        exchange = new HeadersExchange(exchangeData.getName());
                        break;
                    default:
                        throw new BusinessException(BusinessErrorEnums.MQ_UNKNOWN_EXCHANGE_TYPE);
                }
                exchangeData.setExchange(exchange);
                rabbitAdmin.declareExchange(exchange);
                exchangeDataMap.put(exchangeData.getId(), exchangeData);
            }
            rabbitMqProperties.setExchangeDataMap(exchangeDataMap);
        }


        // 创建队列并绑定交换器
        List<RabbitMqProperties.QueueData> queueDataList = rabbitMqProperties.getQueueDataList();
        if (exchangeDataList.size() > 0 && queueDataList.size() > 0){
            Map<String, RabbitMqProperties.QueueData> queueDataMap = new HashMap<>(queueDataList.size());
            for (RabbitMqProperties.QueueData queueData : queueDataList){
                if (Objects.isNull(queueData.getTtl())){

                }
                Queue queue = new Queue(queueData.getQueueName(), true, false, false);
                AbstractExchange exchange = rabbitMqProperties.getExchangeDataMap().get(queueData.getExchangeId()).getExchange();
                Binding binding = BindingBuilder.bind(queue).to(exchange).with(queueData.getRoutingKey()).noargs();
                rabbitAdmin.declareQueue(queue);
                rabbitAdmin.declareBinding(binding);
                queueDataMap.put(queueData.getId(), queueData);
            }
            rabbitMqProperties.setQueueDataMap(queueDataMap);
        }

    }

}
