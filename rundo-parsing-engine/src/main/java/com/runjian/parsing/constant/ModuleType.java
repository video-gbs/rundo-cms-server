package com.runjian.parsing.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/2/10 10:53
 */
@Getter
@AllArgsConstructor
public enum ModuleType {

    GATEWAY(1),
    STREAM(2);

    private final Integer code;
}
