package com.runjian.device.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 流媒体管理中心远程调用
 * @author Miracle
 * @date 2023/1/11 10:32
 */
@FeignClient(value = "stream-manage")
public interface StreamManageApi {



}
