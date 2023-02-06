package com.runjian.device.feign;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.expansion.vo.request.feign.DeviceReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 流媒体管理中心远程调用
 * @author Miracle
 * @date 2023/1/11 10:32
 */
@FeignClient(value = "device-control")
public interface DeviceControlApi {


    @PostMapping("/device/north/add")
    CommonResponse<Long> deviceAdd(DeviceReq deviceReq);

}
