package com.runjian.parsing.mq.listener;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.parsing.constant.MqConstant;
import com.runjian.parsing.dao.DispatchMapper;
import com.runjian.parsing.dao.GatewayMapper;
import com.runjian.parsing.entity.DispatchInfo;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.mq.config.RabbitMqConfig;
import com.runjian.parsing.mq.config.RabbitMqProperties;
import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2022/5/25 10:59
 */
@Data
@Configuration
public class MqListenerConfig {

    @Autowired
    private PublicMsgListener publicMsgListener;

    @Autowired
    private GatewayMsgListener gatewayMsgListener;

    @Autowired
    private StreamMsgListener streamMsgListener;

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Autowired
    private GatewayMapper gatewayMapper;

    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    @Autowired
    private MqDefaultProperties mqDefaultProperties;

    public static Map<String, SimpleMessageListenerContainer> containerMap = new HashMap<>(3);

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
        container.setQueueNames(rabbitMqProperties.getQueueData(mqDefaultProperties.getPublicGetQueue()).getQueueName());
        container.setMessageListener(publicMsgListener);
        container.setConcurrentConsumers(10);
        container.setMaxConcurrentConsumers(10);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        containerMap.put(MqConstant.PUBLIC_PREFIX, container);
        return container;
    }

    /**
     * 配置网关私有消息监听器
     * @param connectionFactory
     * @return
     * @throws BusinessException
     */
    @Bean("gatewayMsgListenerContainer")
    public SimpleMessageListenerContainer gatewayMsgListenerContainer(ConnectionFactory connectionFactory) throws BusinessException {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(gatewayMsgListener);
        container.setConcurrentConsumers(10);
        container.setMaxConcurrentConsumers(10);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        containerMap.put(MqConstant.GATEWAY_PREFIX, container);
        // 将全部网关加载
        List<GatewayInfo> gatewayInfoList = gatewayMapper.selectAll();
        for (GatewayInfo gatewayInfo : gatewayInfoList){
            addQueue(MqConstant.GATEWAY_PREFIX, mqDefaultProperties.getGatewayExchangeId(), MqConstant.GATEWAY_PREFIX + MqConstant.SET_GET_PREFIX + gatewayInfo.getId());
            addQueue(MqConstant.GATEWAY_PREFIX, mqDefaultProperties.getGatewayExchangeId(), MqConstant.GATEWAY_PREFIX + MqConstant.GET_SET_PREFIX + gatewayInfo.getId());
        }
        return container;
    }

    /**
     * 配置流媒体管理服务私有消息监听器
     * @param connectionFactory
     * @return
     * @throws BusinessException
     */
    @Bean("streamMsgListenerContainer")
    public SimpleMessageListenerContainer streamMsgListenerContainer(ConnectionFactory connectionFactory) throws BusinessException {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(streamMsgListener);
        container.setConcurrentConsumers(10);
        container.setMaxConcurrentConsumers(10);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        containerMap.put(MqConstant.STREAM_PREFIX, container);
        // 将全部网关加载
        List<DispatchInfo> dispatchInfoList = dispatchMapper.selectAll();
        for (DispatchInfo dispatchInfo : dispatchInfoList){
            addQueue(MqConstant.STREAM_PREFIX, mqDefaultProperties.getStreamExchangeId(), MqConstant.STREAM_PREFIX + MqConstant.SET_GET_PREFIX + dispatchInfo.getId());
            addQueue(MqConstant.STREAM_PREFIX, mqDefaultProperties.getStreamExchangeId(), MqConstant.STREAM_PREFIX + MqConstant.GET_SET_PREFIX + dispatchInfo.getId());
        }
        return container;
    }

    private void addQueue(String containerName, String exchangeId, String queueKey){
        if (Objects.isNull(containerName)) {
            throw new BusinessException(BusinessErrorEnums.MQ_CONTAINER_NOT_FOUND);
        }
        Queue queue = rabbitMqConfig.addQueue(exchangeId, queueKey, 15000);
        MqListenerConfig.containerMap.get(containerName).addQueues(queue);
    }

}
