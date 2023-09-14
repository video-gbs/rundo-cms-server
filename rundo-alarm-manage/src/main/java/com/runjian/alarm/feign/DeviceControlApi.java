package com.runjian.alarm.feign;

import com.runjian.alarm.vo.request.PutDefenseReq;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "device-control", decode404 = true)
public interface DeviceControlApi {

    /**
     * 布防撤防
     * @param req 布防请求体
     * @return
     */
    @PutMapping("/defenses")
    CommonResponse<?> defense(@RequestBody PutDefenseReq req);
}
