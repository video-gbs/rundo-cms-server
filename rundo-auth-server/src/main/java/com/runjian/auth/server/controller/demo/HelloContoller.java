package com.runjian.auth.server.controller.demo;

import com.runjian.auth.server.common.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName HelloContoller
 * @Description 演示
 * @date 2023-01-11 周三 10:54
 */
@RestController("/demo")
public class HelloContoller {


    @RequestMapping("/hello")
    public ResponseResult hello() {
        String str = "hello，world";
        return new ResponseResult<>(200, "操作成功", str);
    }
}
