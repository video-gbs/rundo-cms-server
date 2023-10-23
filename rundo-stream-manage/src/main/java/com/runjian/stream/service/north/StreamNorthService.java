package com.runjian.stream.service.north;


import com.alibaba.fastjson2.JSONObject;
import com.runjian.stream.entity.StreamInfo;
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
     * 流分发处理
     * @param code
     * @param protocol
     * @param transferMode
     * @param port
     * @param ip
     * @param streamType
     * @param enableAudio
     * @param ssrcCheck
     * @param recordState
     * @param autoCloseState
     * @return
     */
    PostVideoPlayRsp customLive(Long dispatchId, Long code, String protocol, Integer transferMode, String port, String ip, Integer streamType, Boolean enableAudio, Boolean ssrcCheck, Integer recordState, Integer autoCloseState);

    /**
     * 申请流
     * @param channelId 通道id
     * @param enableAudio 是否播放音频
     * @param ssrcCheck 是否使用ssrc
     * @param recordState 是否开启录播
     * @param autoCloseState 是否无人观看
     * @return  PostVideoPlayRsp
     */
    PostVideoPlayRsp streamLivePlay(Long channelId, Integer streamMode, Boolean enableAudio, Boolean ssrcCheck, Integer recordState, Integer autoCloseState, Integer bitStreamId);

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
    PostVideoPlayRsp streamRecordPlay(Long channelId, Integer streamMode, Boolean enableAudio, Boolean ssrcCheck, Integer playType, Integer recordState, Integer autoCloseState, LocalDateTime startTime, LocalDateTime endTime, Integer bitStreamId);

    /**
     * 下载录像视频
     * @param channelId 通道id
     * @param streamMode 码流类型
     * @param enableAudio 是否播放音频
     * @param playType 播放类型
     * @param startTime 播放时间
     * @param endTime 结束时间
     * @param uploadId 上传id
     * @param uploadUrl 上传地址
     * @return 流id
     */
    String downloadRecord(Long channelId, Integer streamMode, Boolean enableAudio, Integer playType, LocalDateTime startTime, LocalDateTime endTime, String uploadId, String uploadUrl);

    /**
     * 下载图片
     * @param channelId 通道id
     * @param streamMode 码流类型
     * @param playType 播放类型
     * @param time 截图时间
     * @param uploadId 上传id
     * @param uploadUrl 上传地址
     * @return 流id
     */
    String downloadImage(Long channelId, Integer streamMode, Integer playType, LocalDateTime time, String uploadId, String uploadUrl);

    /**
     * webRTC推送音频
     * @param channelId 通道id
     * @param recordState 是否开启录播
     * @param autoCloseState 是否自动关闭
     * @return
     */
    String webRtcAudio(Long channelId, Integer recordState, Integer autoCloseState);

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
