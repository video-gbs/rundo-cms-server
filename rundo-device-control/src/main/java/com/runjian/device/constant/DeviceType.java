package com.runjian.device.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/12/13 18:06
 */
@Getter
@AllArgsConstructor
public enum DeviceType {

    DVR(1),
    NVR(2),
    CVR(3),
    DVS(4),
    IPC(5),
    PLATFORM(6);

    private final Integer code;
}
