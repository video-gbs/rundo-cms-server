package com.runjian.device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.runjian.*"})
public class DeviceControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceControlApplication.class, args);
    }
}
