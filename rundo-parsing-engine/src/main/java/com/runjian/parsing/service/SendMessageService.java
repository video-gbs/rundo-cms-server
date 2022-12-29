package com.runjian.parsing.service;

import java.time.LocalDateTime;

public interface SendMessageService {

    /**
     * 点播
     */
    Object playVideo(Long chId);

    /**
     * 回播
     */
    Object playbackVideo(Long chId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 关闭播放
     */

}
