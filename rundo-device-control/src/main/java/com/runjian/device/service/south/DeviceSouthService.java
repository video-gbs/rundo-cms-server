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
     * @param id 设备id
     * @param gatewayId 网关id
     * @param originId 原始id
     * @param onlineState 在线状态
     * @param deviceType 设备类型
     * @param ip ip地址
     * @param port 端口
     */
    void signIn(Long id, Long gatewayId, String originId, Integer onlineState, Integer deviceType, String ip, String port);

}
