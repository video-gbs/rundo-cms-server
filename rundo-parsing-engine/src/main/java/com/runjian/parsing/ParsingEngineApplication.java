package com.runjian.parsing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@SpringBootApplication
@ComponentScan(value = {"com.runjian.*"})
public class ParsingEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParsingEngineApplication.class, args);
    }

}
