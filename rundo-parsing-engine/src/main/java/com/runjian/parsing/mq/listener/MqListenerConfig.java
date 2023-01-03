package com.runjian.parsing.mq.listener;

import com.runjian.common.config.exception.BusinessException;
import com.runjian.parsing.mq.config.RabbitMqProperties;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Miracle
 * @date 2022/5/25 10:59
 */
@Configuration
public class MqListenerConfig {

    @Autowired
    private PublicMsgListener publicMsgListener;

    @Autowired
    private DispatchMsgListener dispatchMsgListener;

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Value("${gateway.public.queue-id-get}")
    private String publicGetQueue;

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
        container.setMessageListener(publicMsgListener);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
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
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        containerMap.put("DISPATCH", container);
        return container;
    }


}
