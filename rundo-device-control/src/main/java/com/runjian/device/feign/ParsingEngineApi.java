package com.runjian.device.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.feign.fallback.ParsingEngineFallback;
import com.runjian.device.vo.feign.DeviceControlReq;
import com.runjian.device.vo.response.VideoRecordRsp;
import com.runjian.device.vo.response.ChannelSyncRsp;
import com.runjian.device.vo.response.DeviceSyncRsp;
import com.runjian.device.vo.response.VideoPlayRsp;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 解析引擎远程调用
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@FeignClient(value = "parsing-engine", fallbackFactory = ParsingEngineFallback.class, decode404 = true)
public interface ParsingEngineApi {

    /**
     * 通用消息处理
     * @param req
     * @return
     */
    @PostMapping("/device-control/custom/event")
    CommonResponse<?> customEvent(@RequestBody DeviceControlReq req);

    /**
     * 网关全量同步
     * @param gatewayIds 网关id数组
     */
    @GetMapping("/device-control/device/total-sync")
    CommonResponse<?> deviceTotalSync(@RequestParam Set<Long> gatewayIds);

}
