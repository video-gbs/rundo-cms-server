package com.runjian.parsing.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MsgType {

    GATEWAY_SIGN_IN("GATEWAY_SIGN_IN"),
    GATEWAY_RE_SIGN_IN("GATEWAY_RE_SIGN_IN"),
    GATEWAY_HEARTBEAT("GATEWAY_HEARTBEAT"),

    DEVICE_SIGN_IN("DEVICE_SIGN_IN"),
    DEVICE_SYNC("DEVICE_SYNC"),

    CHANNEL_PLAY("CHANNEL_PLAY"),
    CHANNEL_PLAYBACK("CHANNEL_PLAYBACK"),
    CHANNEL_STOP_PLAY("CHANNEL_STOP_PLAY"),
    CHANNEL_PTZ_CONTROL("CHANNEL_PTZ_CONTROL"),

    STREAM_SIGN_IN("STREAM_SIGN_IN"),
    STREAM_HEARTBEAT("STREAM_HEARTBEAT"),

    ERROR("ERROR"),
    OTHER("OTHER"),
    ;

    private final String msg;

}
