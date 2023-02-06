package com.runjian.device.expansion.vo.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通道回播请求
 * @author Miracle
 * @date 2023/1/12 15:55
 */
@Data
public class PutChannelPlaybackReq {

    /**
     * 通道id
     */
    private Long chId;

    /**
     * 是否播放音频
     */
    private Boolean enableAudio;

    /**
     * 是否使用ssrc
     */
    private Boolean ssrcCheck;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}
