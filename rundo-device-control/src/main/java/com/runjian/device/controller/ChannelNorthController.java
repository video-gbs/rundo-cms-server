package com.runjian.device.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.device.service.north.ChannelNorthService;
import com.runjian.device.vo.request.PutChannelSignSuccessReq;
import com.runjian.device.vo.response.ChannelSyncRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通道北向控制器
 * @author Miracle
 * @date 2023/1/10 10:05
 */
@RestController
@RequestMapping("/channel/north")
public class ChannelNorthController {

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private ChannelNorthService channelNorthService;

    /**
     * 通道同步
     * @param deviceId 设备ID
     * @return
     */
    @GetMapping("/sync")
    public CommonResponse<ChannelSyncRsp> channelSync(@RequestParam Long deviceId){
        return CommonResponse.success(channelNorthService.channelSync(deviceId));
    }

    /**
     * 通道注册状态转为成功
     * @param request 通道注册状态转为成功请求体
     */
    @PutMapping("/sign/success")
    public CommonResponse channelSignSuccess(@RequestBody PutChannelSignSuccessReq request){
        validatorService.validateRequest(request);
        channelNorthService.channelSignSuccess(request.getChannelId());
        return CommonResponse.success();
    }
}
