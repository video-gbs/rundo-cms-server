package com.runjian.device.expansion;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author chenjialing
 */
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(value = {"com.runjian.*"})
@MapperScan(basePackages = "com.runjian.**.mapper")
public class ExpansionServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpansionServerApplication.class, args);
    }

}
