package com.runjian.device.service.north;

import com.runjian.device.vo.response.DeviceSyncRsp;

/**
 * 设备北向服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
public interface DeviceNorthService {

    /**
     * 设备添加注册
     */
    void deviceAdd(String deviceId, Long gatewayId, Integer online, Integer deviceType, String ip, String port, String name, String manufacturer, String model, String firmware, Integer ptzType);

    /**
     * 设备
     */

    /**
     * 设备同步
     */
    DeviceSyncRsp deviceSync(Long id);

    /**
     * 删除设备
     */
    void delDevice(Long id);


}
