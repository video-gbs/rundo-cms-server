package com.runjian.device.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.service.south.DeviceSouthService;
import com.runjian.device.vo.request.PostChannelSubscribeReq;
import com.runjian.device.vo.request.PostDeviceSignInReq;
import com.runjian.device.vo.request.PostNodeSubscribeReq;
import com.runjian.device.vo.request.PostNodeSyncReq;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/device/south")
public class DeviceSouthController {

    private final ValidatorService validatorService;

    private final DeviceSouthService deviceSouthService;

    /**
     * 设备主动注册
     * @param req 设备注册请求体
     * @return
     */
    @PostMapping("/sign-in")
    public CommonResponse<?> signIn(@RequestBody PostDeviceSignInReq req){
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
    public CommonResponse<?> signInBatch(@RequestBody List<PostDeviceSignInReq> req){
        validatorService.validateRequest(req);
        deviceSouthService.signInBatch(req);
        return CommonResponse.success();
    }

    /**
     * 节点同步
     * @param req 节点同步请求体
     * @return
     */
    @PostMapping("/node/sync")
    public CommonResponse<?> syncNode(@RequestBody PostNodeSyncReq req){
        validatorService.validateRequest(req);
        validatorService.validateRequest(req.getNodeReqList());
        deviceSouthService.nodeSync(req.getDeviceId(), req.getNodeReqList());
        return CommonResponse.success();
    }

    /**
     * 通道订阅
     * @param req 通道订阅请求体
     * @return
     */
    @PostMapping("/channel/subscribe")
    public CommonResponse<?> subscribeChannel(@RequestBody PostChannelSubscribeReq req){
        validatorService.validateRequest(req);
        validatorService.validateRequest(req.getChannelDetailReqList());
        deviceSouthService.channelSubscribe(req.getDeviceId(), req.getSubscribeType(), req.getChannelDetailReqList());
        return CommonResponse.success();
    }

    /**
     * 节点订阅
     * @param req 节点订阅请求体
     * @return
     */
    @PostMapping("/node/subscribe")
    public CommonResponse<?> subscribeNode(@RequestBody PostNodeSubscribeReq req){
        validatorService.validateRequest(req);
        validatorService.validateRequest(req.getPostNodeReqList());
        deviceSouthService.nodeSubscribe(req.getDeviceId(), req.getSubscribeType(), req.getPostNodeReqList());
        return CommonResponse.success();

    }
}
