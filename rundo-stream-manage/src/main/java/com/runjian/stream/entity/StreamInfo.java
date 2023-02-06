package com.runjian.stream.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流信息
 * @author Miracle
 * @date 2023/2/2 15:50
 */
@Data
public class StreamInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 网关id
     */
    private Long gatewayId;

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 流id
     */
    private String streamId;

    /**
     * 调度服务id
     */
    private Long dispatchId;

    /**
     * 播放类型 1-直播 2-录播 3-下载
     */
    private Integer playType;

    /**
     * 录像状态 0-关闭 1-开启
     */
    private Integer recordState;

    /**
     * 是否无人观看时自动关闭 0-关闭 1-开启
     */
    private Integer autoCloseState;

    /**
     * 流状态 0-准备中 1-播放中
     */
    private Integer streamState;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
