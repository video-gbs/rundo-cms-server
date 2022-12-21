package com.runjian.parsing.feign;


import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.feign.request.PostGatewaySignInReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "device-control")
public interface DeviceControlApi {

    @PostMapping("/gateway/south/sign-in")
    CommonResponse gatewaySignIn(@RequestBody PostGatewaySignInReq req);
}
