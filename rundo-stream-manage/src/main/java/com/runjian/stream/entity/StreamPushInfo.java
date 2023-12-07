package com.runjian.stream.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 流推送信息
 * @author Miracle
 * @date 2023/12/6 17:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamPushInfo {


    /**
     * 主键id
     */
    private Long id;

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * ssrc
     */
    private String ssrc;

    /**
     * 流id
     */
    private String streamId;

    /**
     * 目标url
     */
    private String dstUrl;

    /**
     * 目标端口
     */
    private Integer dstPort;

    /**
     * 源端口
     */
    private Integer srcPort;

    /**
     * 状态 0：初始化 1：推送中
     */
    private Integer state;

    /**
     * 传输模式 0：udp,1:tcp被动，2：tcp主动
     */
    private Integer transferMode;

    /**
     * 录播开始时间
     */
    private LocalDateTime startTime;

    /**
     * 录播结束时间
     */
    private LocalDateTime endTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
