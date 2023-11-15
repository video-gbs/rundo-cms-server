package com.runjian.timer.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/9/6 15:30
 */
@Data
public class TemplateUseInfo {

    /**
     * 数据id
     */
    private Long id;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务使用的数据id
     */
    private String serviceUseMark;

    /**
     * 是否启用定时器
     */
    private Integer enableTimer;

    /**
     * 是否已初始化定时器
     */
    private Integer isInitTimer;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
