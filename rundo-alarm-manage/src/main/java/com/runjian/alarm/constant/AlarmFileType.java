package com.runjian.alarm.constant;

import com.runjian.common.constant.MsgType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/9/12 16:36
 */
@Getter
@AllArgsConstructor
public enum AlarmFileType {

    UNKNOWN(0),
    PHOTO(1),
    VIDEO(2);

    private final Integer code;

    public static AlarmFileType getByCode(int code) {
        for (AlarmFileType alarmFileType : AlarmFileType.values()) {
            if (alarmFileType.getCode().equals(code)){
                return alarmFileType;
            }
        }
        return UNKNOWN;
    }
}
