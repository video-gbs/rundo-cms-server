package com.runjian.parsing.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskLogInfo {

    /**
     * 主键id
     */
    private Long id;


    /**
     * 网关id
     */
    private Long gatewayId;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 消息id
     */
    private Long msgId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 状态
     */
    private Integer state;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
