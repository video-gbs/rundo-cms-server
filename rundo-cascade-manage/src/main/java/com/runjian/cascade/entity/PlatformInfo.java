package com.runjian.cascade.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/12/11 16:57
 */
@Data
public class PlatformInfo {

    /**
     * 平台id
     */
    private Long id;

    /**
     * 平台名称
     */
    private String name;

    /**
     * 平台ip
     */
    private String ip;

    /**
     * 平台端口
     */
    private Integer port;

    /**
     * 国标编码
     */
    private String gbCode;

    /**
     * 传输协议
     * 0:UDP 1:TCP
     */
    private Integer sipTransport;

    /**
     * 上下线状态
     */
    private Integer onlineState;

    /**
     * 注册状态
     */
    private Integer signState;

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
