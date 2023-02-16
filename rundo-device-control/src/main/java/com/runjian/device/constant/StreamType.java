package com.runjian.device.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 流模式
 * @see com.runjian.device.entity.ChannelInfo#streamMode
 * @author Miracle
 * @date 2023/1/13 10:16
 */
@Getter
@AllArgsConstructor
public enum StreamType {

    UDP(1,"UDP"),
    TCP(2,"TCP")

    ;

    private final Integer code;
    private final String msg;


    public static String getMsgByCode(Integer code) {
        if (Objects.isNull(code) || code.equals(UDP.code)){
            return UDP.msg;
        }else {
            return TCP.msg;
        }
    }
}
