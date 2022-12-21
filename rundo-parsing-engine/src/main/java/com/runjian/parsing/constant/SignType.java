package com.runjian.parsing.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SignType {

    MQ(1, "MQ"),
    RESTFUL(2, "RESTFUL");

    private final Integer code;

    private final String msg;


}
