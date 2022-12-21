package com.runjian.device.service.south;

import com.runjian.device.entity.GatewayInfo;

public interface GatewaySouthService {

    /**
     * 网关注册
     */
    void signIn(GatewayInfo gatewayInfo);

    /**
     * 网关同步
     */
    void update(GatewayInfo gatewayInfo);

    /**
     * 网关配置
     */


}
