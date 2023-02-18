package com.runjian.device.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
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
    @NotNull(message = "通道id不能为空")
    @Range(min = 1, message = "非法通道id")
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
     * 流模式
     */
    @Range(min = 1, max = 2, message = "非法流模式")
    private Integer streamType;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

}
