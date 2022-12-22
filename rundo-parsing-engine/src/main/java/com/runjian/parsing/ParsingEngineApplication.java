package com.runjian.parsing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ParsingEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParsingEngineApplication.class, args);
    }

}
