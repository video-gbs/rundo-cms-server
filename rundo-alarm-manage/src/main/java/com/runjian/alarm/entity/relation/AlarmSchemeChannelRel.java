package com.runjian.alarm.entity.relation;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/9/8 14:34
 */
@Data
public class AlarmSchemeChannelRel {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 预案Id
     */
    private Long schemeId;

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 布防状态
     */
    private Integer deployState;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
