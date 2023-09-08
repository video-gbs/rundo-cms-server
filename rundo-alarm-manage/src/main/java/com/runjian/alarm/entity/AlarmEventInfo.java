package com.runjian.alarm.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/9/8 17:02
 */
@Data
public class AlarmEventInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 事件排序
     */
    private Integer eventSort;

    /**
     * 事件描述
     */
    private String eventDesc;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
