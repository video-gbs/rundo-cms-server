package com.runjian.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName RestTemplateConfiguration
 * @Description RestTemplate
 * @date 2022-12-28 周三 15:18
 */
@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
