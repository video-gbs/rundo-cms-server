package com.runjian.device;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.List;

/**
 * @author chenjialing
 */
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
@ComponentScan(value = {"com.runjian.*"})
// 开启服务注册功能配置功能
@EnableDiscoveryClient
@MapperScan(basePackages = "com.runjian.**.mapper")
public class ExpansionServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpansionServerApplication.class, args);
    }

}
