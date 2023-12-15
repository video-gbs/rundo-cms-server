package com.runjian.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/12/15 10:20
 */
@Getter
@AllArgsConstructor
public enum SubscribeType {

    ONLINE(1),
    OFFLINE(2),
    ADD(3),
    DELETE(4),
    UPDATE(5),
    UNKNOWN(0);


    private final Integer code;

    public static SubscribeType getType(Integer code) {

        for (SubscribeType type : SubscribeType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
