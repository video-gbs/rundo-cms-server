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
    void deviceAdd(String deviceId, Long gatewayId, Integer deviceType,
                   String ip, String port, String name, String manufacturer, String model, String firmware, Integer ptzType, String username, String password);

    /**
     * 设备注册状态修改
     */
    void deviceSignSuccess(Long deviceId);

    /**
     * 设备同步
     */
    DeviceSyncRsp deviceSync(Long deviceId);

    /**
     * 删除设备
     */
    void deviceDelete(Long deviceId);


}
