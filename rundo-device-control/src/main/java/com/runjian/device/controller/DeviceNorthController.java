package com.runjian.device.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.service.north.DeviceNorthService;
import com.runjian.device.vo.request.PostDeviceAddReq;
import com.runjian.device.vo.request.PutDeviceSignSuccessReq;
import com.runjian.device.vo.response.DeviceSyncRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Miracle
 * @date 2023/1/9 15:18
 */
@RestController
@RequestMapping("/device/north")
public class DeviceNorthController {

    @Autowired
    private DeviceNorthService deviceNorthService;

    @Autowired
    private ValidatorService validatorService;

    /**
     * 设备添加
     * @param req 设备添加请求体
     * @return
     */
    @PostMapping("/add")
    public CommonResponse<?> deviceAdd(@RequestBody PostDeviceAddReq req){
        validatorService.validateRequest(req);
        deviceNorthService.deviceAdd(req.getDeviceId(), req.getGatewayId(), req.getDeviceType(), req.getIp(), req.getPort(), req.getName(), req.getManufacturer(), req.getModel(), req.getFirmware(), req.getPtzType(), req.getUsername(), req.getPassword());
        return CommonResponse.success();
    }

    /**
     * 设备同步
     * @param deviceId 设备id
     * @return
     */
    @GetMapping("/sync")
    public CommonResponse<DeviceSyncRsp> deviceSync(@RequestParam Long deviceId){
        return CommonResponse.success(deviceNorthService.deviceSync(deviceId));
    }

    /**
     * 设备注册成功状态转变
     * @param request 设备注册状态转为成功请求体
     * @return
     */
    @PutMapping("/sign/success")
    public CommonResponse deviceSignSuccess(@RequestBody PutDeviceSignSuccessReq request){
        validatorService.validateRequest(request);
        deviceNorthService.deviceSignSuccess(request.getDeviceId());
        return CommonResponse.success();
    }

    /**
     * 删除设备
     * @param deviceId 设备id
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> deviceDelete(@RequestParam Long deviceId){
        deviceNorthService.deviceDelete(deviceId);
        return CommonResponse.success();
    }



}
