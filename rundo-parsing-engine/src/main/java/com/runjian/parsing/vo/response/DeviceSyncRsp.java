package com.runjian.parsing.vo.response;

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
    private Long id;

    /**
     * 设备类型 1-设备 2-NVR 3-DVR 4-CVR
     */
    private Integer deviceType;

    /**
     * 在线状态 0-离线 1-在线
     */
    private Integer onlineState;

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

}