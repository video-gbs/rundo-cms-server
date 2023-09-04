package com.runjian.utils;

import com.runjian.common.utils.SpringContextUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
@ComponentScan(value = {"com.runjian.*"})
public class TimerUtilsApplication {

    public static void main(String[] args) {
        SpringContextUtils.setAc(SpringApplication.run(TimerUtilsApplication.class, args));
    }

}
