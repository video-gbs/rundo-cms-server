package com.runjian.cascade.service;

import com.runjian.cascade.gb28181Module.gb28181.bean.DeviceChannel;

import java.util.List;

public interface GbPlatformService {
    /**
     * 获取平台下的共享通道
     *
     * @param platformGbCode  上级平台国标编码
     * @return
     */
    List<DeviceChannel> getPlatformNodeAndChannel(String platformGbCode);
}