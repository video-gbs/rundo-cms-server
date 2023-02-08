package com.runjian.parsing.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 调度服务信息
 * @author Miracle
 * @date 2023/2/7 18:09
 */
@Data
public class DispatchInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 序列号
     */
    private String serialNum;

    /**
     * 注册类型
     */
    private Integer signType;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
