package com.runjian.device.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 详细信息
 */
@Data
public class DetailInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 类型 1-通道 2-设备
     */
    private Integer type;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 名称
     */
    private String name;

    /**
     * 厂商
     */
    private String manufacturer;

    /**
     * 型号
     */
    private String model;

    /**
     * 固件版本
     */
    private String firmware;

    /**
     * 云台类型
     */
    private Integer ptzType;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
