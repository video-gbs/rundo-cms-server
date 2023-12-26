package com.runjian.cascade.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/12/26 17:15
 */
@Getter
@AllArgsConstructor
public enum PlatformSignState {

    DELETE(-1),
    LOGOUT(0),
    SIGN_IN(1);

    private final int code;

}
