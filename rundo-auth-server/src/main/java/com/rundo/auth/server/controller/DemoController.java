package com.rundo.auth.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName DemoController
 * @Description 演示类
 * @date 2022-12-27 周二 9:48
 */
@RestController
@RequestMapping("/")
public class DemoController {

    @PostMapping("hello")
    public String sayHello(String name) {

        return name + ",Hello World!";
    }
}
