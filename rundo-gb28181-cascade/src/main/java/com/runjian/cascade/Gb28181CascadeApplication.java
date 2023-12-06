package com.runjian.cascade;

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
public class Gb28181CascadeApplication {
    public static void main(String[] args) {

        SpringApplication.run(Gb28181CascadeApplication.class);
    }
}