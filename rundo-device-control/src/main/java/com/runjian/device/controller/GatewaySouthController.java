package com.runjian.device.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.service.south.GatewaySouthService;
import com.runjian.device.vo.request.PostGatewaySignInReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        gatewaySouthService.signIn(request.toGatewayInfo());
        return CommonResponse.success();
    }

}
