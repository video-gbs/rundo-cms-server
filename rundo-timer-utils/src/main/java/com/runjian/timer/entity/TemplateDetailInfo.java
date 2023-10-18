package com.runjian.timer.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author Miracle
 * @date 2023/9/4 17:30
 */
@Data
public class TemplateDetailInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 时间类型 1 星期一 2 星期二 3 星期三 4 星期四 5 星期五 6 星期六 7 星期日
     */
    private Integer dateType;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     *  结束时间
     */
    private  LocalTime endTime;

    /**
     * 是否延续到下一天
     */
    private Integer isNextDay;

    /**
     * 是否启用定时器
     */
    private Integer enableTimer;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
