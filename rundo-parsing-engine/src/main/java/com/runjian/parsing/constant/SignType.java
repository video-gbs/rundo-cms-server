package com.runjian.parsing.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 注册类型
 * @see com.runjian.parsing.entity.GatewayInfo#signType
 * @author Miracle
 * @date 2023/1/12 9:43
 */
@Getter
@AllArgsConstructor
public enum SignType {

    MQ(1, "MQ"),
    RESTFUL(2, "HTTP");

    private final Integer code;

    private final String msg;


}
