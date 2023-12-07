package com.runjian.stream.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/12/7 16:16
 */
@Getter
@AllArgsConstructor
public enum StreamPushState {

    INIT(0),
    PUSH(1),
    DELETE(-1);

    private final Integer code;
}
