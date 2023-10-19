package com.runjian.device.expansion.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/10/19 15:13
 */
@Data
public class GetAlarmDeviceChannelRsp {

    /**
     * 通道id
     */
    private Long id;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 设备名称
     */
    private Long deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 在线状态
     */
    private Integer onlineState;

}
