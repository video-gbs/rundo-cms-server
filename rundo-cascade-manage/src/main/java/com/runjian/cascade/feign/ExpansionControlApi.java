package com.runjian.cascade.feign;

import com.runjian.cascade.feign.fallback.DeviceControlFallback;
import com.runjian.cascade.feign.fallback.ExpansionControlFallback;
import com.runjian.cascade.vo.feign.resp.ChannelExpansionResp;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "device-expansion", fallbackFactory = ExpansionControlFallback.class, decode404 = true)
public interface ExpansionControlApi {

    /**
     * 扩展服务 批量获取通道信息
     * @param channelIds
     * @return
     */
    @GetMapping(value = "/expansion/channel/internal/getListBatch")
    public CommonResponse<List<ChannelExpansionResp>> getListBatch(@RequestParam  List<Long> channelIds);
}
