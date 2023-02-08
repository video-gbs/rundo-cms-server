package com.runjian.parsing.vo.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SignInRsp {

    /**
     * 是否第一次注册
     */
    private Boolean isFirstSignIn;

    /**
     * 网关id
     */
    @JsonIgnore
    private Long gatewayId;

    /**
     * 调度服务id
     */
    @JsonIgnore
    private Long dispatchId;

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
