package com.runjian.device.expansion.vo.response;

import lombok.Data;

import java.util.List;

/**
 * 用户主动同步返回值
 * @author Miracle
 * @date 2023/1/9 9:55
 */
@Data
public class ChannelSyncRsp {

    /**
     * 设备通道总数
     */
    private Integer total;

    /**
     * 同步成功的通道数
     */
    private Integer num;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 同步的数据
     */
    private List<ChannelDetailRsp> channelDetailList;


}
