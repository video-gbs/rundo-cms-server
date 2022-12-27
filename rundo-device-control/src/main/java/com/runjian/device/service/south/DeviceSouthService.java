package com.runjian.device.service.south;

import com.runjian.device.entity.DeviceInfo;

public interface DeviceSouthService {

    /**
     * 设备注册
     */
    void signIn(DeviceInfo deviceInfo);

    /**
     * 设备同步
     */
    void update(DeviceInfo deviceInfo);
}
