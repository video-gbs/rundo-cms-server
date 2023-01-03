package com.runjian.auth.server.controller;

import com.runjian.common.config.response.CommonResponse;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 区域通道关联表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@RestController
@RequestMapping("/authServer/areaChannel")
public class AreaChannelController {

    @RequestMapping
    public CommonResponse<String> SayHello(String name){
        return CommonResponse.success(name + ",hello world");
    }
}
