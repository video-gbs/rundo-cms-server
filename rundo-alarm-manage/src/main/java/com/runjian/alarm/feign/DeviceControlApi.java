package com.runjian.alarm.feign;

import com.runjian.alarm.feign.fallback.DeviceControlFallback;
import com.runjian.alarm.vo.feign.PostMessageSubReq;
import com.runjian.alarm.vo.feign.PostMessageSubRsp;
import com.runjian.alarm.vo.request.PutDefenseReq;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(value = "device-control", fallbackFactory = DeviceControlFallback.class, decode404 = true)
public interface DeviceControlApi {

    /**
     * 布防撤防
     * @param req 布防请求体
     * @return 失败的通道id
     */
    @PutMapping("/channel/north/defenses")
    CommonResponse<Set<Long>> defense(@RequestBody PutDefenseReq req);

    /**
     * 信息订阅
     * @param req
     * @return
     */
    @PostMapping("/message/sub")
    CommonResponse<List<PostMessageSubRsp>> subMsg(@RequestBody PostMessageSubReq req);
}
