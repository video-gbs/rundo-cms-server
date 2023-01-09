package com.runjian.device.service.south;

/**
 * 通道南向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
public interface ChannelSouthService {


    /**
     * 通道同步
     */
    void channelSync(Long deviceId);
}
