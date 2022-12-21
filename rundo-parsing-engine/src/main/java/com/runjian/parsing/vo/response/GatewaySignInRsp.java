package com.runjian.parsing.vo.response;

import lombok.Data;

@Data
public class GatewaySignInRsp {

    /**
     * 是否第一次注册
     */
    private Boolean isFirstSignIn;

    /**
     * 网关id
     */
    private Long gatewayId;

    /**
     * 注册类型 MQ RESTFUL
     */
    private String signType;

    /**
     * 交换器
     */
    private String mqExchange;

    /**
     * 发送消息
     */
    private String mqSetQueue;

    /**
     * 监听消息
     */
    private String mqGetQueue;
}
