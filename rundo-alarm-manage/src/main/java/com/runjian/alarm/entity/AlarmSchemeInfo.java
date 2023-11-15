package com.runjian.alarm.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 告警预案信息
 * @author Miracle
 * @date 2023/9/8 11:13
 */
@Data
public class AlarmSchemeInfo {

    /**
     * 预案id
     */
    private Long id;

    /**
     * 预案名称
     */
    private String schemeName;

    /**
     * 时间模板id
     */
    private Long templateId;

    /**
     * 是否禁用
     */
    private Integer disabled;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
