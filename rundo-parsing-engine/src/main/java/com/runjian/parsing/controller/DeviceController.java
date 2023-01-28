package com.runjian.parsing.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.parsing.constant.IdType;
import com.runjian.parsing.service.ProtocolService;
import com.runjian.parsing.vo.request.PostDeviceAddReq;
import com.runjian.parsing.vo.response.DeviceSyncRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * 设备控制控制器
 * @author Miracle
 * @date 2023/1/12 10:00
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    private static final long OUT_TIME = 5000L;

    @Autowired
    private ProtocolService protocolService;


    @Autowired
    private ValidatorService validatorService;


    /**
     * 设备信息同步
     * @param deviceId 设备id
     * @return 设备同步消息体
     */
    @GetMapping("/device/sync")
    public DeferredResult<CommonResponse<?>> deviceSync(@RequestParam Long deviceId){
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        protocolService.getProtocol(deviceId, IdType.DEVICE).deviceSync(deviceId, response);
        return response;
    }

    /**
     * 设备添加
     * @param req 设备添加请求体
     * @return 设备id
     */
    @PostMapping("/device/add")
    public DeferredResult<CommonResponse<?>> deviceAdd(@RequestBody PostDeviceAddReq req){
        validatorService.validateRequest(req);
        final DeferredResult<CommonResponse<?>> response = new DeferredResult<>(OUT_TIME);
        //protocolService.getProtocol(req.getGatewayId(), IdType.GATEWAY).deviceAdd(req.getGatewayId(), req.getDeviceId(), response);
        return response;
    }

    /**
     * 设备删除
     * @param deviceId 设备id
     * @return 删除结果
     */
    @DeleteMapping("/device/delete")
    DeferredResult<CommonResponse<Boolean>> deviceDelete(@RequestParam Long deviceId){
        final DeferredResult<CommonResponse<Boolean>> response = new DeferredResult<>(OUT_TIME);

        return response;
    }

}
