package com.runjian.parsing.vo.request;

import lombok.Data;

@Data
public class GatewaySignInReq {


    /**
     * 协议
     */
    private String protocol;

    /**
     * 网关类型 {@link com.runjian.parsing.constant.GatewayType}
     */
    private String gatewayType;

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

}
