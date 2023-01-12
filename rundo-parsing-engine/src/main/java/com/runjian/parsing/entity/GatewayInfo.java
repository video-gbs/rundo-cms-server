package com.runjian.parsing.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 网关映射表
 * @author Miracle
 * @date 2023/1/12 9:43
 */
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
     * 注册类型 {@link com.runjian.parsing.constant.SignType}
     */
    private Integer signType;

    /**
     * 网关类型 {@link com.runjian.parsing.constant.GatewayType}
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

    private LocalDateTime createTime;

}
