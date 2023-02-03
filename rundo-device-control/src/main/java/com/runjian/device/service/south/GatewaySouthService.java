package com.runjian.device.service.south;

import com.runjian.device.entity.GatewayInfo;

import java.time.LocalDateTime;

/**
 * 网关南向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
public interface GatewaySouthService {

    /**
     * 网关心跳处理
     */
    void heartbeat();

    /**
     * 网关注册
     * @param gatewayInfo 网关信息
     */
    void signIn(GatewayInfo gatewayInfo, LocalDateTime outTime);



    /**
     * 网关同步
     * @param gatewayInfo 网关信息
     */
    void update(GatewayInfo gatewayInfo);

    /**
     * 更新心跳信息
     * @param gatewayId 网关ID
     * @param outTime 过期时间
     */
    void updateHeartbeat(Long gatewayId, LocalDateTime outTime);

}
