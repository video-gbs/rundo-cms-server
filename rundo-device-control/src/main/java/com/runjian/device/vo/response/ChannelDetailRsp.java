package com.runjian.device.vo.response;

import lombok.Data;

/**
 * 通道详情
 * @author Miracle
 * @date 2023/1/9 10:16
 */
@Data
public class ChannelDetailRsp {

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 通道不见
     */
    private Integer channelType;

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
