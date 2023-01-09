package com.runjian.device.service.south;

import com.runjian.device.entity.DeviceInfo;

/**
 * 设备南向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
public interface DeviceSouthService {

    /**
     * 设备添加注册
     */
    void signIn(Long id, Long gatewayId, Integer online, Integer deviceType, String ip, String port);

}
