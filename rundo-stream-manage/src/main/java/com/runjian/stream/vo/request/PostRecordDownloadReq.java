package com.runjian.stream.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.runjian.common.constant.PlayType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/9/14 17:39
 */
@Data
public class PostRecordDownloadReq {

    /**
     * 通道id不能为空
     */
    @NotNull(message = "通道id不能为空")
    @Range(min = 1, message = "非法通道id")
    private Long channelId;

    /**
     * 是否播放音频
     */
    @NotNull(message = "播放音频选项不能为空")
    private Boolean enableAudio;

    /**
     * 流类型 (1-udp 2-tcp)
     */
    @NotNull(message = "流类型不能为空")
    private Integer streamType;

    /**
     * 播放模式 {@link PlayType}
     */
    @NotNull(message = "播放模式不能为空")
    @Range( min = 2, max = 4, message = "非法播放模式")
    private Integer playType;

    /**
     * 录像开始时间
     */
    @PastOrPresent(message = "时间必须是过去的时间")
    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime startTime;

    /**
     * 录像结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime endTime;

    /**
     * 上传id
     */
    @NotBlank(message = "上传id不能为空")
    private String uploadId;

    /**
     * 上传url
     */
    @NotBlank(message = "上传url不能为空")
    private String uploadUrl;
}