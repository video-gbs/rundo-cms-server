package com.runjian.device.expansion.vo.feign.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 获取原始待添加的通道
 * @author chenjialing
 */
@Data
public class GetChannelByPageRsp {

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 原始id
     */
    private String originId;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
