package com.runjian.parsing.constant;

import com.runjian.parsing.vo.CommonMqDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型
 * @see CommonMqDto#msgType
 * @author Miracle
 * @date 2023/1/12 9:43
 */

@Getter
@AllArgsConstructor
public enum MsgType {

    /****************** 网关相关消息 ******************/
    GATEWAY_SIGN_IN("GATEWAY_SIGN_IN"),
    GATEWAY_RE_SIGN_IN("GATEWAY_RE_SIGN_IN"),
    GATEWAY_HEARTBEAT("GATEWAY_HEARTBEAT"),

    /****************** 调度相关消息 ******************/

    DISPATCH_SIGN_IN("DISPATCH_SIGN_IN"),

    DISPATCH_RE_SIGN_IN("DISPATCH_RE_SIGN_IN"),

    DISPATCH_HEARTBEAT("DISPATCH_HEARTBEAT"),

    /****************** 设备相关消息 ******************/
    DEVICE_SIGN_IN("DEVICE_SIGN_IN"),
    DEVICE_SYNC("DEVICE_SYNC"),
    DEVICE_DELETE("DEVICE_DELETE"),
    DEVICE_ADD("DEVICE_ADD"),

    /****************** 通道相关消息 ******************/
    CHANNEL_SYNC("CHANNEL_SYNC"),
    CHANNEL_PLAY("CHANNEL_PLAY"),
    CHANNEL_PLAYBACK("CHANNEL_PLAYBACK"),
    CHANNEL_STOP_PLAY("CHANNEL_STOP_PLAY"),
    CHANNEL_PTZ_CONTROL("CHANNEL_PTZ_CONTROL"),
    CHANNEL_RECORD_INFO("CHANNEL_RECORD_INFO"),

    /****************** 流媒体管理服务相关消息 ******************/
    STREAM_SIGN_IN("STREAM_SIGN_IN"),
    STREAM_HEARTBEAT("STREAM_HEARTBEAT"),

    /****************** 其他 ******************/
    ERROR("ERROR"),

    ;
    private final String msg;

}
