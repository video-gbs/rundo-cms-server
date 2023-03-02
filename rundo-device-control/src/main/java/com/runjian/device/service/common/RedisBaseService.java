package com.runjian.device.service.common;

import java.util.Map;

/**
 * @author Miracle
 * @date 2023/2/23 11:05
 */
public interface RedisBaseService {

    /**
     * 更新通道的上线状态
     * @param channelOnlineStateMap
     */
    void batchUpdateChannelOnlineState(Map<Long, Integer> channelOnlineStateMap);

    /**
     * 更新设备的在线状态
     * @param deviceOnlineStateMap
     */
    void batchUpdateDeviceOnlineState(Map<Long, Integer> deviceOnlineStateMap);

    /**
     * 更新设备的在线状态
     * @param deviceId 设备id
     * @param onlineState 在线状态
     */
    void updateDeviceOnlineState(Long deviceId,  Integer onlineState);
}
