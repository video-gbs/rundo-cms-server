package com.runjian.stream.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.stream.vo.StreamManageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/2/6 15:27
 */
@FeignClient(value = "parsing-engine")
public interface ParsingEngineApi {


    /**
     * 通用消息处理
     * @param req
     * @return
     */
    @PostMapping("/stream-manage/custom/event")
    CommonResponse<?> streamCustomEvent(@RequestBody StreamManageDto req);

    /**
     * 删除所有的流
     * @param dispatchIds 调度服务id数组
     */
    @DeleteMapping("/stream-manage/stream/stop/all")
    CommonResponse<?> streamStopAll(@RequestParam Set<Long> dispatchIds);

}
