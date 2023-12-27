package com.runjian.cascade.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "device-control", fallbackFactory = DeviceControlFallback.class, decode404 = true)
public interface DeviceControlApi {

}
