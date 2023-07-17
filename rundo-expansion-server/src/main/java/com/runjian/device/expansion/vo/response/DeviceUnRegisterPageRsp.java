package com.runjian.device.expansion.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/2/6 16:19
 */
@Data
public class DeviceUnRegisterPageRsp {

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 网关id
     */
    private Long gatewayId;

    /**
     * 原始id
     */
    private String originId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 网关名称
     */
    private String gatewayName;

    /**
     * 注册状态
     */
    private Integer signState;

    /**
     * 在线状态
     */
    private Integer onlineState;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
