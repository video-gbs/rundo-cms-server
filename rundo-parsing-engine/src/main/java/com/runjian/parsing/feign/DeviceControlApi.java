package com.runjian.parsing.feign;


import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.feign.request.PostGatewaySignInReq;
import com.runjian.parsing.vo.request.PutGatewayHeartbeatReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "device-control")
public interface DeviceControlApi {

    @PostMapping("/gateway/south/sign-in")
    CommonResponse<?> gatewaySignIn(@RequestBody PostGatewaySignInReq req);

    @PutMapping("/gateway/south/heartbeat")
    CommonResponse<?> gatewayHeartbeat(@RequestBody PutGatewayHeartbeatReq req);

    @PostMapping("/device/south/sign-in")
    CommonResponse<?> deviceSignIn(JSONObject req);

    @PostMapping("/common/south/event")
    CommonResponse<?> commonEvent(JSONObject req);
}
