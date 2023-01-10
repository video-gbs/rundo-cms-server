package com.runjian.device.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.service.south.GatewaySouthService;
import com.runjian.device.vo.request.PostGatewaySignInReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 网关南向控制器
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@RestController
@RequestMapping("/gateway/south")
public class GatewaySouthController {

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private GatewaySouthService gatewaySouthService;

    @PostMapping("/sign-in")
    public CommonResponse signIn(@RequestBody PostGatewaySignInReq request){
        validatorService.validateRequest(request);
        gatewaySouthService.signIn(request.toGatewayInfo(), request.getOutTime());
        return CommonResponse.success();
    }

}
