package com.runjian.stream.service.north;


import com.runjian.stream.entity.StreamInfo;
import com.runjian.stream.vo.response.PostApplyStreamRsp;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 获取流信息
     * @param streamIds 流id数组
     * @param recordState 录像状态
     * @param streamState 流状态
     * @return
     */
    List<StreamInfo> getRecordStates(List<String> streamIds, Integer recordState, Integer streamState);

    /**
     * 调整播放速度
     * @param streamId 流id
     * @param speed 速递
     */
    void speedRecord(String streamId, Float speed);

    /**
     * 拖动时间
     * @param streamId 流id
     * @param currentTime 现在播放时间
     * @param targetTime 目标播放时间
     */
    void seekRecord(String streamId, LocalDateTime currentTime, LocalDateTime targetTime);

    /**
     * 暂停录像播放
     * @param streamId 流id
     */
    void pauseRecord(String streamId);

    /**
     * 恢复录像播放
     * @param streamId 流id
     */
    void resumeRecord(String streamId);

}
