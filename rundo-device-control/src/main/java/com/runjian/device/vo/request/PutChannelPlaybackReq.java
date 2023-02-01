package com.runjian.device.vo.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    private Long channelId;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

}
