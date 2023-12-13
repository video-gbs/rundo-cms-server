package com.runjian.cascade.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/12/11 16:57
 */
@Data
public class PlatformSupInfo {

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
     * 国标域
     */
    private String gbDomain;

    /**
     * 国标编码
     */
    private String gbCode;

    /**
     * 上下线状态
     */
    private Integer onlineState;

    /**
     * 注册状态
     */
    private Integer signState;

    /**
     * 是否订阅下级
     */
    private Integer isSubscribe;

    /**
     * 流传输模式
     */
    private Integer streamTransferType;

    /**
     * 挂载节点
     */
    private String mountNode;

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
