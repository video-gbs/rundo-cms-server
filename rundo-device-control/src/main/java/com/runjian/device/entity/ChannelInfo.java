package com.runjian.device.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通道信息
 */
@Data
public class ChannelInfo {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 注册状态 0-已注册 1-待注册 2-待添加 {@link com.runjian.device.constant.SignState}
     */
    private Integer signState;

    /**
     * 在线状态 0-离线 1-在线 {@link com.runjian.common.constant.CommonEnum}
     */
    private Integer onlineState;

    /**
     * 通道类型 1-通道 2-设备
     */
    private Integer channelType;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
