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
     * 通道id
     */
    private Long channelId;

    /**
     * 播放模式 {@link com.runjian.common.constant.PlayType}
     */
    private Integer playType;

    /**
     * 录像状态
     */
    private Integer recordState;

    /**
     * 自动关闭状态
     */
    private Integer autoCloseState;
}
