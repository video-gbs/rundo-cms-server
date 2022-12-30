package com.runjian.auth.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AuthServerApplication
 * @Description 应用启动类
 * @date 2022-12-22 周四 14:41
 */
@SpringBootApplication
// 开启服务注册功能配置功能
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.runjian.auth.server.service")
@MapperScan(basePackages = "com.runjian.auth.server.mapper")
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);

    }

}
