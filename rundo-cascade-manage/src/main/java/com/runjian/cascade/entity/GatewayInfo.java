package com.runjian.cascade.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/12/11 14:37
 */
@Data
public class GatewayInfo {

    /**
     * 网关id
     */
    private Long id;

    /**
     * 网关名称
     */
    private String name;

    /**
     * 网关ip
     */
    private String ip;

    /**
     * 网关端口
     */
    private Integer port;

    /**
     * 国标编码
     */
    private String gbCode;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
