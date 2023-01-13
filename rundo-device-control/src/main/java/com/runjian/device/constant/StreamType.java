package com.runjian.device.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流模式
 * @see com.runjian.device.entity.ChannelInfo#streamMode
 * @author Miracle
 * @date 2023/1/13 10:16
 */
@Getter
@AllArgsConstructor
public enum StreamType {

    UDP("UDP"),
    TCP("TCP")

    ;

    private final String msg;
}
