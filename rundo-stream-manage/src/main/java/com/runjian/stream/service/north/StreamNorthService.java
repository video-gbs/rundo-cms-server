package com.runjian.stream.service.north;


import com.alibaba.fastjson2.JSONObject;
import com.runjian.stream.entity.StreamInfo;
import com.runjian.stream.vo.response.PostApplyStreamRsp;
import com.runjian.stream.vo.response.PostVideoPlayRsp;

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
     * @param channelId 通道id
     * @param enableAudio 是否播放音频
     * @param ssrcCheck 是否使用ssrc
     * @param recordState 是否开启录播
     * @param autoCloseState 是否无人观看
     * @return  PostVideoPlayRsp
     */
    PostVideoPlayRsp streamLivePlay(Long channelId, Boolean enableAudio, Boolean ssrcCheck, Integer recordState, Integer autoCloseState);

    /**
     * 录播播放
     * @param channelId 通道id
     * @param enableAudio 是否播放音频
     * @param ssrcCheck 是否使用ssrc
     * @param playType 播放类型
     * @param recordState 是否开启录播
     * @param autoCloseState 是否无人观看
     * @param startTime 播放时间
     * @param endTime 结束时间
     * @return PostVideoPlayRsp
     */
    PostVideoPlayRsp streamRecordPlay(Long channelId, Boolean enableAudio, Boolean ssrcCheck, Integer playType, Integer recordState, Integer autoCloseState, LocalDateTime startTime, LocalDateTime endTime);


    /**
     * 停止播放
     * @param streamId 通道id
     */
    void stopPlay(String streamId);

    /**
     * 开启录像
     * @param streamId 通道id
     */
    Boolean startRecord(String streamId);

    /**
     * 关闭录像
     * @param streamId 通道id
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
     * @param speed 速度
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

    /**
     * 获取流信息
     * @param streamId 流id
     * @return
     */
    JSONObject getStreamMediaInfo(String streamId);

}
