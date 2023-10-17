package com.runjian.device.expansion.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/9/11 11:08
 */
@Data
public class GetAlarmSchemeChannelRsp {

    /**
     * 通道id
     */
    private Long channelId;

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

    /**
     * 预案id
     */
    private Long schemeId;

    /**
     * 预案名称
     */
    private String schemeName;

}
