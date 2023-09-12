package com.runjian.alarm.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2022/9/16 14:40
 */
@Getter
@AllArgsConstructor
public enum PhotoState {

    INIT(0, "等待中"),
    GENERATING(1, "截图中"),
    SUCCESS(2, "生成成功"),
    ERROR(-1, "异常"),

    ;

    private final Integer code;

    private final String msg;
}