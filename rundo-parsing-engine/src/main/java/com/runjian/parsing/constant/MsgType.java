package com.runjian.parsing.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MsgType {

    SIGN_IN("SIGN_IN"),
    RE_SIGN_IN("RE_SIGN_IN"),

    HEARTBEAT("HEARTBEAT"),

    PLAY("PLAY"),
    PLAYBACKE("PLAYBACK"),
    STOP_PLAY("STOP_PLAY"),

    ERROR("ERROR"),
    OTHER("OTHER"),
    ;

    private final String msg;

}
