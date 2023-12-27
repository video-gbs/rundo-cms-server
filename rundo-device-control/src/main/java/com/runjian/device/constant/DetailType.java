package com.runjian.device.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 详细数据类型
 * @see  com.runjian.device.entity.DetailInfo#type
 * @author Miracle
 * @date 2023/1/9 18:04
 */
@Getter
@AllArgsConstructor
public enum DetailType {
    DEVICE(1, "DEVICE"),
    CHANNEL(2, "CHANNEL"),
    PLATFORM(3, "PLATFORM");

    private final Integer code;

    private final String msg;


}
