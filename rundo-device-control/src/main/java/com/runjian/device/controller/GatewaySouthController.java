package com.runjian.device.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.service.south.GatewaySouthService;
import com.runjian.device.vo.request.PostGatewaySignInReq;
import com.runjian.device.vo.request.PutHeartbeatReq;
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

    /**
     * 网关注册
     * @param request 网关注册请求体
     * @return
     */
    @PostMapping("/sign-in")
    public CommonResponse signIn(@RequestBody PostGatewaySignInReq request){
        validatorService.validateRequest(request);
        gatewaySouthService.signIn(request.toGatewayInfo(), request.getOutTime());
        return CommonResponse.success();
    }

    /**
     * 心跳信息处理
     * @param request 心跳信息
     * @return
     */
    @PutMapping("/heartbeat")
    public CommonResponse heartbeat(@RequestBody PutHeartbeatReq request){
        validatorService.validateRequest(request);
        gatewaySouthService.updateHeartbeat(request.getGatewayId(), request.getOutTime());
        return CommonResponse.success();
    }
}
