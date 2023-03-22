package com.runjian.parsing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@ComponentScan(value = {"com.runjian.*"})
public class ParsingEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParsingEngineApplication.class, args);
    }

}
