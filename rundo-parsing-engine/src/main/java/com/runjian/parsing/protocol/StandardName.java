package com.runjian.parsing.protocol;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/2/1 18:13
 */
@Data
public class StandardName {

    /**
     * 标准设备Id名称
     */
    public static final String DEVICE_ID = "deviceId";

    /**
     * 标准通道Id名称
     */
    public String CHANNEL_ID = "channelId";

    /**
     * 标准网关名称
     */
    public String GATEWAY_ID = "gatewayId";

    /**
     * 标准消息类型名称
     */
    public String MSG_TYPE = "msgType";

    /**
     * 标准通道详情列表名称
     */
    public String CHANNEL_SYNC_LIST = "channelDetailList";

    /**
     * 标准通道类型名称
     */
    public String CHANNEL_TYPE = "channelType";

    /**
     * 标准在线状态名称
     */
    public String ONLINE_STATE = "onlineState";

    /**
     * 标准设备或通道的名称
     */
    public String DEVICE_CHANNEL_NAME = "name";

    /**
     * 标准ip名称
     */
    public String IP = "ip";
}
