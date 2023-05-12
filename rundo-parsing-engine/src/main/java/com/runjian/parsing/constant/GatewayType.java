package com.runjian.parsing.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;


/**
 * 网关类型
 * @see com.runjian.parsing.entity.GatewayInfo#gatewayType
 * @author Miracle
 * @date 2023/1/12 9:43
 */
@Getter
@AllArgsConstructor
public enum GatewayType {
    CHANNEL(1, "DEVICE"),
    DEVICE(2, "CHANNEL"),
    NVR(3, "NVR"),
    DVR(4, "DVR"),
    CVR(5, "CVR"),
    OTHER(6, "OTHER")

    ;

    private final Integer code;

    private final String msg;


    public static Integer getCodeByMsg(String msg){
        if(Objects.isNull(msg)){
            return OTHER.code;
        }
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
