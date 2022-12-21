package com.runjian.parsing.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GatewayInfo {

    /**
     * 数据Id
     */
    private Long id;

    /**
     * 网关注册序列号
     */
    private String serialNum;

    /**
     * 注册类型 1-MQ  2-RETFUL
     */
    private Integer signType;

    /**
     * 网关类型
     */
    private Integer gatewayType;

    /**
     * 网关协议
     */
    private String protocol;

    /**
     * 网关ip地址
     */
    private String ip;

    /**
     * 网关端口
     */
    private String port;

    private LocalDateTime updateTime;

    private LocalDateTime createTimel;

}
