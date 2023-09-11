package com.runjian.alarm.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/9/11 16:45
 */

@Getter
@AllArgsConstructor
public enum EventEndType {

    SINGLE(1),
    START_END(2),
    ;

    private final Integer code;
}
