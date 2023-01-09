package com.runjian.device.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DetailType {

    DEVICE(1, "DEVICE"),
    CHANNEL(2, "CHANNEL");

    private final Integer code;

    private final String msg;


}
