package com.runjian.alarm.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/9/11 16:45
 */

@Getter
@AllArgsConstructor
public enum EventMsgType {


    SINGLE(0),
    COMPOUND_START(1),
    COMPOUND_HEARTBEAT(2),
    COMPOUND_END(3)
    ;

    private final Integer code;

    public static EventMsgType getByCode(int code) {
        for (EventMsgType eventMsgType : EventMsgType.values()) {
            if (eventMsgType.getCode().equals(code)){
                return eventMsgType;
            }
        }
        return SINGLE;
    }
}
