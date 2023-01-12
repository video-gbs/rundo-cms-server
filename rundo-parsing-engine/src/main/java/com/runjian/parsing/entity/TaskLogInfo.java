package com.runjian.parsing.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务日志映射表
 * @author Miracle
 * @date 2023/1/12 9:43
 */
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
