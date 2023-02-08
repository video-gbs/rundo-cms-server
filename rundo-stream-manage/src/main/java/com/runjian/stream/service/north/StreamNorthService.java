package com.runjian.stream.service.north;


import com.runjian.stream.vo.response.PostApplyStreamRsp;

/**
 * 流
 * @author Miracle
 * @date 2023/2/3 10:34
 */
public interface StreamNorthService {

    /**
     * 申请流
     */
    PostApplyStreamRsp applyStreamId(Long gatewayId, Long channelId, Integer playType, Integer recordState, Integer autoCloseState);

    /**
     * 停止播放
     */
    void stopPlay(String streamId);

    /**
     * 开启录像
     */
    Boolean startRecord(String streamId);

    /**
     * 关闭录像
     */
    Boolean stopRecord(String streamId);

}
