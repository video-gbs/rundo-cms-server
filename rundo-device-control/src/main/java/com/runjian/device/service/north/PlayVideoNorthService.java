package com.runjian.device.service.north;

import com.runjian.device.vo.response.VideoPlayRsp;

import java.time.LocalDateTime;

public interface PlayVideoNorthService {

    /**
     * 点播
     * @param chId 通道id
     */
    VideoPlayRsp play(Long chId);

    /**
     * 回放
     * @param chId 通道id
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    VideoPlayRsp playBack(Long chId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 停播
     * @param chId 通道id
     */
    void stopPlay(Long chId);
}
