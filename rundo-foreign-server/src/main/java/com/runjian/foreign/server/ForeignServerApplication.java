package com.runjian.foreign.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author chenjialing
 */
@RefreshScope
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(value = {"com.runjian.*"})
@MapperScan(basePackages = "com.runjian.**.mapper")
public class ForeignServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForeignServerApplication.class, args);
    }
}
