package com.runjian.parsing.service;

import com.runjian.parsing.vo.GatewayMqDto;

public interface DeviceService {

    /**
     * 设备注册
     */
    Boolean deviceSignIn(GatewayMqDto gatewayMqDto);

    /**
     * 设备同步
     */
    Boolean deviceSync();


}
