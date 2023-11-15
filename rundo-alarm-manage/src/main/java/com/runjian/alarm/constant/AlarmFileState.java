package com.runjian.alarm.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2022/9/16 14:40
 */
@Getter
@AllArgsConstructor
public enum AlarmFileState {

    INIT(0, "初始化"),
    WAITING(1, "等待中"),
    GENERATING(2, "生成中"),
    SUCCESS(3, "生成成功"),
    ERROR(-1, "异常"),

    ;

    private final Integer code;

    private final String msg;
}