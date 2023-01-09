package com.runjian.device.service.north;

import com.runjian.device.vo.response.ChannelSyncRsp;

/**
 * @author Miracle
 * @date 2023/1/9 9:53
 */
public interface ChannelNorthService {

    /**
     * 通道同步
     * @param deviceId
     * @return
     */
    ChannelSyncRsp channelSync(Long deviceId);
}
