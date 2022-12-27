package com.runjian.device.service.north;

public interface PtzNorthService {

    /**
     * 云台控制
     * @param chId 通道id
     * @param command 控制指令,允许值: left, right, up, down, upleft, upright, downleft, downright, zoomin, zoomout, stop
     * @param horizonSpeed 水平速度
     * @param verticalSpeed 垂直速度
     * @param zoomSpeed 缩放速度
     */
    void ptzControl(Long chId, String command, int horizonSpeed, int verticalSpeed, int zoomSpeed);
}
