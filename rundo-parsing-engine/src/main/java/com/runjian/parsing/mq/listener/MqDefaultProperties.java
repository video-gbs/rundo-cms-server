package com.runjian.parsing.mq.listener;

import com.runjian.parsing.mq.config.RabbitMqConfig;
import com.runjian.parsing.mq.config.RabbitMqProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Miracle
 * @date 2023/2/13 9:57
 */
@Data
@Configuration
@RequiredArgsConstructor
public class MqDefaultProperties {

    private final RabbitMqProperties rabbitMqProperties;

    private final RabbitMqConfig rabbitMqConfig;

    @Value("${mq-default.public.queue-id-get}")
    private String publicGetQueue;

    @Value("${mq-default.public.queue-id-set}")
    private String publicSetQueue;

    @Value("${mq-default.exchange.stream}")
    private String streamExchangeId;

    @Value("${mq-default.exchange.gateway}")
    private String gatewayExchangeId;

    private RabbitMqProperties.QueueData publicSetQueueData;

    @PostConstruct
    public void init() {
        publicSetQueueData = rabbitMqProperties.getQueueData(publicSetQueue);
    }
}
