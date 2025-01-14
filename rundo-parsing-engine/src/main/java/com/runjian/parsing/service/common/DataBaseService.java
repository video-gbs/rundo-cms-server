package com.runjian.parsing.service.common;

import com.runjian.parsing.entity.ChannelInfo;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.entity.DispatchInfo;
import com.runjian.parsing.entity.GatewayInfo;

/**
 * @author Miracle
 * @date 2023/1/17 16:27
 */
public interface DataBaseService {

    /**
     * 获取网关信息
     * @param gatewayId
     * @return
     */
    GatewayInfo getGatewayInfo(Long gatewayId);

    /**
     * 获取设备信息
     * @param deviceId
     * @return
     */
    DeviceInfo getDeviceInfo(Long deviceId);

    /**
     * 获取通道信息
     * @param channelId
     * @return
     */
    ChannelInfo getChannelInfo(Long channelId);

    /**
     * 获取流媒体服务
     * @param dispatchId 流媒体服务id
     * @return
     */
    DispatchInfo getDispatchInfo(Long dispatchId);
}
