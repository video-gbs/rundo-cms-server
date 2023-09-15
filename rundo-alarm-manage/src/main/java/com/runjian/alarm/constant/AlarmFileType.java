package com.runjian.alarm.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/9/12 16:36
 */
@Getter
@AllArgsConstructor
public enum AlarmFileType {

    UNKNOWN(0, "UNKNOWN"),
    IMAGE(1, "IMAGE"),
    VIDEO(2, "VIDEO");

    private final Integer code;

    private final String msg;

    public static AlarmFileType getByCode(int code) {
        for (AlarmFileType alarmFileType : AlarmFileType.values()) {
            if (alarmFileType.getCode().equals(code)){
                return alarmFileType;
            }
        }
        return UNKNOWN;
    }
}
