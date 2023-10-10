package com.runjian.parsing.feign;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.feign.fallback.AlarmManageFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Miracle
 * @date 2023/9/18 17:03
 */
@FeignClient(value = "alarm-manage", fallbackFactory = AlarmManageFallback.class, decode404 = true)
public interface AlarmManageApi {

    /**
     * 告警信息接收
     * @param jsonObject 网关请求体
     * @return
     */
    @PostMapping("/msg/south/receive")
    CommonResponse<?> receiveAlarmMsg(JSONObject jsonObject);
}
