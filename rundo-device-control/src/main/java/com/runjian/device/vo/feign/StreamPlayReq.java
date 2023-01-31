package com.runjian.device.vo.feign;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/1/30 14:35
 */
@Data
public class StreamPlayReq {

    /**
     * 网关id
     */
    private Long gatewayId;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 是否是回放
     */
    private Boolean isPlayback;
}
