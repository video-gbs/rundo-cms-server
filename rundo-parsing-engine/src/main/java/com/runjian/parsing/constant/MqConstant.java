package com.runjian.parsing.constant;

import lombok.Data;

@Data
public class MqConstant {

    /**
     * 网关通用MQ前缀
     */
    public static final String GATEWAY_PREFIX = "GATEWAY_";

    /**
     * 网关GET,服务端SET前缀
     */
    public static final String GET_SET_PREFIX = "GS_";

    /**
     * 网关SET,服务端GET前缀
     */
    public static final String SET_GET_PREFIX = "SG_";
}
