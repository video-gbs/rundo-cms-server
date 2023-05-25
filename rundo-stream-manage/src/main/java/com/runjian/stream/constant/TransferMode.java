package com.runjian.stream.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/4/25 14:19
 */
@Getter
@AllArgsConstructor
public enum TransferMode {

    PUSH(1),
    PULL(2);


    private final Integer code;


}
