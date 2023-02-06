package com.runjian.device.expansion.entity;

import com.runjian.device.expansion.constant.SignState;
import com.runjian.device.expansion.constant.StreamType;
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
     * 注册状态 0-已注册 1-待注册 2-待添加 {@link SignState}
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

    /**
     * 流模式: TCP & UCP {@link StreamType}
     */
    private String streamMode;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
