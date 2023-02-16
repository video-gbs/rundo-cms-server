package com.runjian.parsing.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/2/10 11:08
 */
@Data
public class StreamTaskInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 流媒体调度服务id
     */
    private Long dispatchId;

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 流id
     */
    private String streamId;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 状态 {@link com.runjian.parsing.constant.TaskState}
     */
    private Integer state;

    /**
     * 详情
     */
    private String detail;

    /**
     * 队列id
     */
    private String mqId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
