package com.runjian.device.service.north;

import com.runjian.device.vo.response.VideoPlayRsp;

import java.time.LocalDateTime;

/**
 * 视频播放北向服务
 * @author Miracle
 * @date 2023/1/12 17:08
 */
public interface PlayVideoNorthService {

    /**
     * 点播
     * @param chId 通道id
     */
    VideoPlayRsp play(Long chId, Boolean enableAudio, Boolean ssrcCheck);

    /**
     * 回放
     * @param chId 通道id
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    VideoPlayRsp playBack(Long chId, Boolean enableAudio, Boolean ssrcCheck, LocalDateTime startTime, LocalDateTime endTime);

}
