package com.rundo.auth.server.controller;

import com.runjian.common.config.response.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("hello")
    public CommonResponse sayHello(String name) {
        return CommonResponse.success(name + ",Hello World!");
    }

    @GetMapping("/hi")
    public CommonResponse list() {
        List<String> list = new ArrayList<>();
        list.add("张三");
        list.add("李四");
        list.add("王二");
        list.add("麻子");
        return CommonResponse.success(list);
    }

}
