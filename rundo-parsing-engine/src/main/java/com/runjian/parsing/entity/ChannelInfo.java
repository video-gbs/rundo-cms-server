package com.runjian.parsing.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通道映射信息表
 * @author Miracle
 * @date 2023/1/12 9:43
 */
@Data
public class ChannelInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 原始通道ID
     */
    private String originId;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
