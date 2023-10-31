package com.runjian.alarm.feign;

import com.runjian.alarm.feign.fallback.TimerUtilsFallback;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/9/8 10:02
 */
@FeignClient(value = "timer-utils", fallbackFactory = TimerUtilsFallback.class, decode404 = true)
public interface TimerUtilsApi {

    /**
     * 检测时间是否可用
     * @param templateId 时间模板id
     * @param time 时间
     * @return
     */
    @GetMapping("/check/time-in")
    CommonResponse<Boolean> checkTime(@RequestParam Long templateId, @RequestParam LocalDateTime time);
}
