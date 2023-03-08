package com.runjian.parsing.feign;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.feign.fallback.StreamManageFallback;
import com.runjian.parsing.vo.request.PostDispatchSignInReq;
import com.runjian.parsing.vo.request.PutDispatchHeartbeatReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author Miracle
 * @date 2023/2/7 18:08
 */
@FeignClient(value = "stream-manage", fallbackFactory = StreamManageFallback.class, decode404 = true)
public interface StreamManageApi {

    @PutMapping("/dispatch/south/heartbeat")
    CommonResponse<Boolean> dispatchHeartbeat(@RequestBody PutDispatchHeartbeatReq putDispatchHeartbeatReq);

    @PostMapping("/dispatch/south/sign-in")
    CommonResponse<?> dispatchSignIn(@RequestBody PostDispatchSignInReq req);

    @PutMapping("/stream/south/play/result")
    CommonResponse<?> streamReceiveResult(@RequestBody JSONObject req);

    @PutMapping("/stream/south/record/result")
    CommonResponse<?> streamReceiveRecordResult(@RequestBody JSONObject req);

    @PutMapping("/stream/south/play/close")
    CommonResponse<Boolean> streamCloseHandle(@RequestBody JSONObject req);
}
