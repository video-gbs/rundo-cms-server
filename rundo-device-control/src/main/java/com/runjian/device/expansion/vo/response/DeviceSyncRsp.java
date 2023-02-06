package com.runjian.device.expansion.vo.response;

import lombok.Data;

/**
 * 设备同步返回体
 * @author Miracle
 * @date 2023/1/9 9:55
 */
@Data
public class DeviceSyncRsp {

    /**
     * 主键id
     */
    private Long deviceId;

    /**
     * 在线状态 0-离线 1-在线
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

}
