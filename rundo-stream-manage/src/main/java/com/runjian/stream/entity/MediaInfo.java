package com.runjian.stream.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流媒体服务信息
 * @author Miracle
 * @date 2023/2/2 15:49
 */
@Data
public class MediaInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 调度服务id
     */
    private Long dispatchId;

    /**
     * 类型 1-zlm
     */
    private Integer type;

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 原始id
     */
    private String originId;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
