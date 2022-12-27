package com.runjian.parsing.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GatewayType {

    DEVICE(1, "DEVICE"),
    NVR(2, "NVR"),
    DVR(3, "DVR"),
    CVR(4, "CVR"),
    OTHER(5, "OTHER")

    ;

    private final Integer code;

    private final String msg;


    public static Integer getCodeByMsg(String msg){
        if (msg.equals(DEVICE.msg)){
            return DEVICE.code;
        } else if (msg.equals(NVR.msg)){
            return NVR.code;
        } else if (msg.equals(DVR.msg)){
            return DVR.code;
        } else if (msg.equals(CVR.msg)) {
            return CVR.code;
        } else {
            return OTHER.code;
        }
    }
}
