package com.runjian.device.expansion.service;

public interface IBaseDeviceAndChannelService {

    /**
     * 删除设备
     * @param id
     */
    void removeDevice(Long id);

    /**
     * 软删除设备
     * @param id
     */
    void removeDeviceSoft(Long id);
}
