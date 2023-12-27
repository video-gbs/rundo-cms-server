package com.runjian.cascade.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/12/26 17:55
 */
@Getter
@AllArgsConstructor
public enum SipTransportType {


    UDP(0, "UDP"),
    TCP(1, "TCP");

    private final int code;

    private final String msg;

    public static String getSipTransportMsg(int code) {
        for (SipTransportType sipTransport : SipTransportType.values()) {
            if (sipTransport.code == code) {
                return sipTransport.msg;
            }
        }
        return null;
    }
}
