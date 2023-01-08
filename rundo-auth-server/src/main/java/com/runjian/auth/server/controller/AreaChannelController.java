package com.runjian.auth.server.controller;

import com.runjian.auth.server.entity.AreaChannel;
import com.runjian.auth.server.service.AreaChannelService;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 区域通道关联表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@RestController
@RequestMapping("/areaChannel")
public class AreaChannelController {

    @Autowired
    private AreaChannelService areaChannelService;


    @GetMapping("/list")
    public CommonResponse<List<AreaChannel>> getAreaChannelList(){
        return CommonResponse.success(areaChannelService.list());
    }
}
