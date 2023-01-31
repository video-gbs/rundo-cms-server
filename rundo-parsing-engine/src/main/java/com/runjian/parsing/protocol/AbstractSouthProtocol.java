package com.runjian.parsing.protocol;

import lombok.Data;

/**
 * 默认的字段名字
 * @author Miracle
 * @date 2023/1/29 20:27
 */
@Data
public abstract class AbstractSouthProtocol implements SouthProtocol {

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
     * 消息类型
     */
    protected String MSG_TYPE = "msgType";

    /**
     * 通道详情列表名称
     */
    protected String CHANNEL_SYNC_LIST = "channelDetailList";

    /**
     * 通道录像列表名称
     */
    protected String CHANNEL_RECORD_LIST = "channelRecordList";



    @Override
    public void channelPtzControl(Long taskId, Object data) {
        customEvent(taskId, data);
    }

    @Override
    public void channelPlay(Long taskId, Object dataMap) {
        customEvent(taskId, dataMap);
    }

    @Override
    public void channelRecord(Long taskId, Object dataMap) {
        customEvent(taskId, dataMap);
    }

    @Override
    public void channelPlayback(Long taskId, Object dataMap) {
        customEvent(taskId, dataMap);
    }


}
