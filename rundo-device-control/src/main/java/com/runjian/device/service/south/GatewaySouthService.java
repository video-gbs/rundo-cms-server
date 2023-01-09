package com.runjian.device.service.south;

import com.runjian.device.entity.GatewayInfo;

/**
 * 网关南向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
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
