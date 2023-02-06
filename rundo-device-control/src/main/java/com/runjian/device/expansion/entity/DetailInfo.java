package com.runjian.device.expansion.entity;

import com.runjian.device.expansion.constant.DetailType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备或者通道的详细信息
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Data
public class DetailInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 设备通道id
     */
    private Long dcId;

    /**
     * 类型 1-通道 2-设备 {@link DetailType}
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
