package com.runjian.alarm.entity.relation;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/9/8 14:34
 */
@Data
public class AlarmSchemeDeviceRel {

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


    private LocalDateTime createTime;
}
