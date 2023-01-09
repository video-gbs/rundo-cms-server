package com.runjian.device.service.north;

/**
 * 设备云台控制服务
 * @author Miracle
 * @date 2023/01/06 16:56
 */
public interface PtzNorthService {

    /**
     * 云台控制
     * @param chId 通道id
     * @param commandCode 控制指令,允许值: left, right, up, down, upleft, upright, downleft, downright, zoomin, zoomout, stop
     * @param horizonSpeed 水平速度
     * @param verticalSpeed 垂直速度
     * @param zoomSpeed 缩放速度
     */
    void ptzControl(Long chId, Integer commandCode, Integer horizonSpeed, Integer verticalSpeed, Integer zoomSpeed, Integer totalSpeed);
}
