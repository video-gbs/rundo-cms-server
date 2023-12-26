package com.runjian.stream.service.north;

import com.runjian.stream.vo.response.PostStreamPushInitRsp;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/12/7 9:26
 */
public interface StreamPushNorthService {

    /**
     * 初始化端口
     */
    void initSrcPort();

    /**
     * 流推送初始化
     * @param channelId
     * @param dstUrl
     * @param dstPort
     * @param transferMode
     * @param startTime
     * @param endTime
     * @return
     */
    PostStreamPushInitRsp streamPushInit(Long channelId, String ssrc, String dstUrl, Integer dstPort, Integer transferMode, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 进行流推送
     * @param streamPushId
     */
    void streamPushRun(Long streamPushId);

    /**
     * 关闭推流
     * @param streamPushId
     */
    void streamPushStop(Long streamPushId);

    /**
     * 检测流推送信息
     */
    void checkStreamPushState();
}
