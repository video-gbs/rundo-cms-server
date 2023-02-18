package com.runjian.device.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/2/10 16:19
 */
@Data
public class GetGatewayPageRsp {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 网关唯一序列号 网关ID
     */
    private String serialNum;

    /**
     * 网关名称
     */
    private String name;

    /**
     * 注册类型 1-MQ  2-RETFUL
     */
    private Integer signType;

    /**
     * 在线状态 0-离线 1-在线 {@link com.runjian.common.constant.CommonEnum}
     */
    private Integer onlineState;

    /**
     * 网关类型
     */
    private Integer gatewayType;

    /**
     * 协议
     */
    private String protocol;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
