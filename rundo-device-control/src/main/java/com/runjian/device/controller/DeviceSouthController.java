package com.runjian.device.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.service.south.DeviceSouthService;
import com.runjian.device.vo.request.PostDeviceSignInReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 设备南向控制器
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@RestController
@RequestMapping("/device/south")
public class DeviceSouthController {


    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private DeviceSouthService deviceSouthService;

    /**
     * 设备主动注册
     * @param req 设备注册请求体
     * @return
     */
    @PostMapping("/sign-in")
    public CommonResponse signIn(@RequestBody PostDeviceSignInReq req){
        validatorService.validateRequest(req);
        deviceSouthService.signIn(req.getDeviceId(), req.getGatewayId(), req.getOriginId(), req.getOnlineState(), req.getDeviceType(), req.getIp(), req.getPort(),
                req.getName(), req.getManufacturer(), req.getModel(), req.getFirmware(), req.getPtzType(), req.getUsername(), req.getPassword());
        return CommonResponse.success();
    }

    /**
     * 设备批量注册
     * @param req 设备主动注册请求体
     * @return
     */
    @PostMapping("/sign-in-batch")
    public CommonResponse signInBatch(@RequestBody List<PostDeviceSignInReq> req){
        validatorService.validateRequest(req);
        deviceSouthService.signInBatch(req);
        return CommonResponse.success();
    }
}
