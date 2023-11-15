package com.runjian.alarm.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/9/13 15:25
 */
@Getter
@AllArgsConstructor
public enum AlarmState {

    UNDERWAY(0, "进行中"),
    SUCCESS(1, "已完成");

    private final Integer code;

    private final String msg;
}
