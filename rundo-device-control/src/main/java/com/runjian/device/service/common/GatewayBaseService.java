package com.runjian.device.service.common;

import com.runjian.common.utils.CircleArray;

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
    void systemStart();

    /**
     * 心跳
     */
    void heartbeat();
}
