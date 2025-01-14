package com.runjian.parsing.constant;

import lombok.Data;

/**
 * MQ前缀
 * @author Miracle
 * @date 2023/1/12 9:43
 */
@Data
public class MqConstant {

    /**
     * 网关通用MQ前缀
     */
    public static final String GATEWAY_PREFIX = "GATEWAY_";

    /**
     * 流媒体服务通用MQ前缀
     */
    public static final String STREAM_PREFIX = "STREAM_";

    /**
     * 公共消息通用MQ前缀
     */
    public static final String PUBLIC_PREFIX = "PUBLIC_";

    /**
     * 网关GET,服务端SET前缀
     */
    public static final String GET_SET_PREFIX = "GS_";

    /**
     * 网关SET,服务端GET前缀
     */
    public static final String SET_GET_PREFIX = "SG_";
}
