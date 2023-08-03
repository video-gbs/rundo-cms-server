package com.runjian.device.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/5/16 14:45
 */
@Getter
@AllArgsConstructor
public enum SubMsgType {

    // 设备上下线状态
    DEVICE_ONLINE_STATE(1, "deviceOnline"),
    // 通道上下线状态
    CHANNEL_ONLINE_STATE(2, "channelOnline"),
    // 设备添加
    DEVICE_ADD_OR_DELETE_STATE(5, "deviceAddOrDelete"),
    // 通道添加
    CHANNEL_ADD_OR_DELETE_STATE(6, "channelAddOrDelete")
    ;


    private final Integer code;

    private final String name;


    public static SubMsgType getByName(String name) {
        if (name == null) {
            return null;
        }
        for (SubMsgType subMsgType : SubMsgType.values()) {
            if (name.equals(subMsgType.getName())){
                return subMsgType;
            }
        }
        return null;
    }

}
