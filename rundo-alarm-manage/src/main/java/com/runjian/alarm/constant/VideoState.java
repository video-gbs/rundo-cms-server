package com.runjian.alarm.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2022/9/16 14:40
 */
@Getter
@AllArgsConstructor
public enum VideoState {

    INIT(0, "初始化"),
    IN_RECORD(1, "录制中"),
    SUCCESS(2, "生成成功"),
    ERROR(-1, "异常"),

    ;

    private final Integer code;

    private final String msg;
}