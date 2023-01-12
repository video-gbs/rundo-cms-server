package com.runjian.parsing.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备映射表
 * @author Miracle
 * @date 2023/1/12 9:43
 */
@Data
public class DeviceInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 网关id
     */
    private Long gatewayId;

    /**
     * 设备id
     */
    private String originId;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
