package com.runjian.device.expansion.constant;

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
    // 设备删除状态
    DEVICE_DELETE_STATE(3, "deviceDelete"),
    // 通道删除
    CHANNEL_DELETE_STATE(4, "channelDelete")
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
