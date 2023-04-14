package com.runjian.stream.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.stream.service.south.DispatchSouthService;
import com.runjian.stream.vo.request.PostDispatchSignInReq;
import com.runjian.stream.vo.request.PutHeartbeatReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Miracle
 * @date 2023/2/3 17:47
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dispatch/south")
public class DispatchSouthController {


    private final ValidatorService validatorService;


    private final DispatchSouthService dispatchSouthService;


    /**
     * 网关注册
     * @param request 网关注册请求体
     * @return
     */
    @PostMapping("/sign-in")
    public CommonResponse<?> signIn(@RequestBody PostDispatchSignInReq request){
        validatorService.validateRequest(request);
        dispatchSouthService.signIn(request.getDispatchId(), request.getSerialNum(), request.getIp(), request.getPort(), request.getOutTime());
        return CommonResponse.success();
    }

    /**
     * 心跳信息处理
     * @param request 心跳信息
     * @return
     */
    @PutMapping("/heartbeat")
    public CommonResponse<Boolean> heartbeat(@RequestBody PutHeartbeatReq request){
        validatorService.validateRequest(request);
        return CommonResponse.success(dispatchSouthService.updateHeartbeat(request.getDispatchId(), request.getOutTime()));
    }

}
