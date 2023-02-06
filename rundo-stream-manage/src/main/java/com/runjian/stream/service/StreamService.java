package com.runjian.stream.service;


/**
 * 流
 * @author Miracle
 * @date 2023/2/3 10:34
 */
public interface StreamService {

    /**
     * 申请流
     */
    String getStreamId(Long gatewayId, Long channelId, Integer playType, Integer recordState, Integer autoCloseState);

    /**
     * 接收流播放结果
     */
    void receiveResult(String streamId, Boolean isSuccess);

    /**
     * 自动关闭
     * @return 是否关闭
     */
    Boolean autoCloseHandle(String streamId);

    /**
     * 停止播放
     */
    void stopPlay(String streamId);

    /**
     * 开启录像
     */
    String startRecord(String streamId);

    /**
     * 关闭录像
     */
    String stopRecord(String streamId);

}
