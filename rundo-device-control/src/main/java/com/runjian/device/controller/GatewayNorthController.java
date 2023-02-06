package com.runjian.device.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.service.north.GatewayNorthService;
import com.runjian.device.vo.response.GetGatewayNameRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/2/6 17:24
 */
@RestController("/gateway/north")
public class GatewayNorthController {

    @Autowired
    private GatewayNorthService gatewayNorthService;


    @GetMapping("/name")
    public CommonResponse<List<GetGatewayNameRsp>> getGatewayName(){
        return CommonResponse.success(gatewayNorthService.getGatewayNameList());
    }
}
