package com.runjian.device.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.tools.Diagnostic;

@Getter
@AllArgsConstructor
public enum SignState {

    SUCCESS(0, "已注册"),
    TO_BE_ADD(1,"待添加"),

    TO_BE_SIGN_IN(2, "待注册"),

    DELETED(-1, "已删除")
    ;


    private final Integer code;

    private final String msg;
}
