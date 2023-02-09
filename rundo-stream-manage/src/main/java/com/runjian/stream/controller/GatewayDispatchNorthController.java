package com.runjian.stream.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.stream.service.north.GatewayDispatchNorthService;
import com.runjian.stream.vo.request.PostDispatchBindingGatewayReq;
import com.runjian.stream.vo.request.PostGatewayBindingDispatchReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Miracle
 * @date 2023/2/9 10:49
 */
@RestController
@RequestMapping("/gateway-dispatch/north")
public class GatewayDispatchNorthController {

    @Autowired
    private GatewayDispatchNorthService gatewayDispatchNorthService;

    @Autowired
    private ValidatorService validatorService;

    /**
     * 网关绑定调度服务
     * @param req 网关绑定流媒体服务请求体
     * @return
     */
    @PostMapping("/gateway/binding")
    public CommonResponse<?> gatewayBindingDispatch(@RequestBody PostGatewayBindingDispatchReq req){
        validatorService.validateRequest(req);
        gatewayDispatchNorthService.gatewayBindingDispatch(req.getGatewayId(), req.getDispatchId());
        return CommonResponse.success();
    }


    /**
     * 调度服务绑定网关
     * @param req 流媒体服务绑定网关请求体
     * @return
     */
    @PostMapping("/dispatch/binding")
    public CommonResponse<?> dispatchBindingGateway(@RequestBody PostDispatchBindingGatewayReq req){
        validatorService.validateRequest(req);
        gatewayDispatchNorthService.dispatchBindingGateway(req.getDispatchId(), req.getGatewayIds());
        return CommonResponse.success();
    }


}
