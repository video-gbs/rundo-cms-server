package com.runjian.parsing.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.vo.feign.PutStreamReceiveResultReq;
import com.runjian.parsing.vo.request.PostDispatchSignInReq;
import com.runjian.parsing.vo.request.PutDispatchHeartbeatReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Miracle
 * @date 2023/2/7 18:08
 */
@FeignClient(value = "stream-manage")
public interface StreamManageApi {

    @PutMapping("/dispatch/south/heartbeat")
    CommonResponse<?> dispatchHeartbeat(PutDispatchHeartbeatReq putDispatchHeartbeatReq);

    @PostMapping("/dispatch/south/sign-in")
    CommonResponse<?> dispatchSignIn(PostDispatchSignInReq req);

    @PutMapping("/stream/south/result")
    CommonResponse<?> streamReceiveResult(PutStreamReceiveResultReq req);

    @GetMapping("/stream/south/close")
    CommonResponse<?> streamCloseHandle(@RequestParam String streamId);
}
