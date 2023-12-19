package com.runjian.device.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/12/19 14:41
 */
@Getter
@AllArgsConstructor
public enum NodeMsgStateType {

    ADD(1),
    DELETE(2),
    UPDATE(3);

    private final Integer code;
}
