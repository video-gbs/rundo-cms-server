package com.runjian.parsing.protocol;

import lombok.Data;

/**
 * 默认的字段名字
 * @author Miracle
 * @date 2023/1/29 20:27
 */
@Data
public class DefaultMapName {

    /**
     * 默认设备Id名称
     */
    protected String DEVICE_ID = "deviceId";

    /**
     * 默认通道Id名称
     */
    protected String CHANNEL_ID = "channelId";

    /**
     * 默认网关名称
     */
    protected String GATEWAY_ID = "gatewayId";

    /**
     * 通道列表名称
     */
    protected String CHANNEL_SYNC_LIST = "channelDetailList";
}
