package com.runjian.device.service.common;

import com.runjian.common.utils.CircleArray;

import java.util.Set;

/**
 * @author Miracle
 * @date 2023/2/17 15:27
 */
public interface GatewayBaseService {

    /**
     * 心跳时钟
     */
    CircleArray<Long> heartbeatArray = new CircleArray<>(600);

    /**
     * 系统启动时关闭所有网关
     */
    void init();

    /**
     * 心跳
     */
    void heartbeat();

    /**
     * 网关下线
     * @param gatewayIds 网关Id
     */
    void gatewayOffline(Set<Long> gatewayIds);

    /**
     * 网关全量数据同步
     */
    void deviceTotalSync();
}
