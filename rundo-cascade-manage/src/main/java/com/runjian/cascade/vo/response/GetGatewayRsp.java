package com.runjian.cascade.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/12/11 15:21
 */
@Data
public class GetGatewayRsp {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 网关id
     */
    private Long gatewayId;

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
