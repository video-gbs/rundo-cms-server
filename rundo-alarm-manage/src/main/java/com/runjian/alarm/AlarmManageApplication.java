package com.runjian.alarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Miracle
 * @date 2023/9/8 9:40
 */
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
@ComponentScan(value = {"com.runjian.*"})
public class AlarmManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlarmManageApplication.class, args);
    }
}
