package com.runjian.parsing.mq.listener;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.parsing.constant.MqConstant;
import com.runjian.parsing.dao.GatewayMapper;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.mq.config.RabbitMqConfig;
import com.runjian.parsing.mq.config.RabbitMqProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2022/5/25 10:59
 */
@Configuration
public class MqListenerConfig {

    @Autowired
    private GatewayPublicMsgListener gatewayPublicMsgListener;

    @Autowired
    private DispatchMsgListener dispatchMsgListener;

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Autowired
    private GatewayMapper gatewayMapper;

    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    @Value("${gateway.public.queue-id-get}")
    private String publicGetQueue;

    @Value("${gateway.public.queue-id-set}")
    private String signInQueueId;


    public static Map<String, SimpleMessageListenerContainer> containerMap = new HashMap<>(2);

    /**
     * 配置公共消息监听器
     * @param connectionFactory
     * @return
     * @throws BusinessException
     */
    @Bean
    public SimpleMessageListenerContainer publicMsgListenerContainer(ConnectionFactory connectionFactory) throws BusinessException {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(rabbitMqProperties.getQueueData(publicGetQueue).getQueueName());
        container.setMessageListener(gatewayPublicMsgListener);
        container.setConcurrentConsumers(10);
        container.setMaxConcurrentConsumers(10);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        containerMap.put("PUBLIC", container);
        return container;
    }

    /**
     * 配置私有消息监听器
     * @param connectionFactory
     * @return
     * @throws BusinessException
     */
    @Bean("dispatchMsgListenerContainer")
    public SimpleMessageListenerContainer dispatchMsgListenerContainer(ConnectionFactory connectionFactory) throws BusinessException {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(dispatchMsgListener);
        container.setConcurrentConsumers(10);
        container.setMaxConcurrentConsumers(10);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        containerMap.put("DISPATCH", container);
        // 将全部网关加载
        RabbitMqProperties.QueueData queueData = rabbitMqProperties.getQueueData(signInQueueId);
        List<GatewayInfo> gatewayInfoList = gatewayMapper.selectAll();
        for (GatewayInfo gatewayInfo : gatewayInfoList){
            addQueue("DISPATCH", queueData.getExchangeId(), MqConstant.GATEWAY_PREFIX + MqConstant.SET_GET_PREFIX + gatewayInfo.getId());
        }
        return container;
    }

    private void addQueue(String containerName, String exchangeId, String queueKey){
        if (Objects.isNull(containerName)) {
            throw new BusinessException(BusinessErrorEnums.MQ_CONTAINER_NOT_FOUND);
        }
        Queue queue = new Queue(queueKey, true, false, false);
        AbstractExchange exchange = rabbitMqProperties.getExchangeDataMap().get(exchangeId).getExchange();
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(queueKey).noargs();
        rabbitMqConfig.addQueue(queue, binding);
        MqListenerConfig.containerMap.get(containerName).addQueues(queue);
    }

}
