package com.runjian.device.expansion.constant;

import com.runjian.device.expansion.entity.ChannelInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流模式
 * @see ChannelInfo#streamMode
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
