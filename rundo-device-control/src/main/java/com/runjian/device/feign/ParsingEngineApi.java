package com.runjian.device.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "parsing-engine")
public interface ParsingEngineApi {


}
