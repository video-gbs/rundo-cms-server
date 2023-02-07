package com.runjian.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
@ComponentScan(value = {"com.runjian.*"})
public class StreamManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamManageApplication.class, args);
    }

}
