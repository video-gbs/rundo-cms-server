package com.runjian.timer.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/9/4 17:29
 */
@Data
public class TemplateInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 模板名称
     */
    private String templateName;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
