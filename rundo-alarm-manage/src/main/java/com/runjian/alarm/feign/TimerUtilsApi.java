package com.runjian.alarm.feign;

import com.runjian.alarm.feign.fallback.TimerUtilsFallback;
import com.runjian.alarm.vo.feign.PostUseTemplateReq;
import com.runjian.alarm.vo.feign.PutUnUseTemplateReq;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

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
    CommonResponse<Boolean> checkTime(@RequestParam Long templateId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime time);

    /**
     * 使用模板
     * @param req 使用模板请求体
     * @return
     */
    @PostMapping("/check/use")
    CommonResponse<?> useTemplate(@RequestBody PostUseTemplateReq req);

    /**
     * 解除模板使用
     * @param req 解除使用模板请求体
     * @return
     */
    @PutMapping("/check/unuse")
    CommonResponse<?> unUseTemplate(@RequestBody PutUnUseTemplateReq req);
}
