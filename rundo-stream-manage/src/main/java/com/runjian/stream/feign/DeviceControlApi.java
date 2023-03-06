package com.runjian.stream.feign;

import com.github.pagehelper.PageInfo;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.stream.feign.fallback.DeviceControlFallback;
import com.runjian.stream.vo.request.PostGetGatewayByDispatchReq;
import com.runjian.stream.vo.response.GetGatewayByIdsRsp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/2/20 19:41
 */
@FeignClient(value = "device-control", fallbackFactory = DeviceControlFallback.class, decode404 = true)
public interface DeviceControlApi {

    @GetMapping("/gateway/north/data/ids")
    CommonResponse<PageInfo<GetGatewayByIdsRsp>> getGatewayByIds(@SpringQueryMap PostGetGatewayByDispatchReq req);

    @GetMapping("/gateway/north/page")
    CommonResponse<PageInfo<GetGatewayByIdsRsp>> getGatewayByName(@RequestParam int page, @RequestParam int num, @RequestParam(required = false) String name);
}
