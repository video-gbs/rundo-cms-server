package com.runjian.stream.vo.request;


import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/2/7 20:39
 */
@Data
public class PostStreamLivePlayReq {

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
     * 是否使用ssrc
     */
    @NotNull(message = "ssrc选项不能为空")
    private Boolean ssrcCheck;

    /**
     * 录像状态
     */
    @NotNull(message = "录像状态不能为空")
    @Range(min = 0, max = 1, message = "非法录像状态")
    private Integer recordState;

    /**
     * 自动关闭状态
     */
    @NotNull(message = "自动关闭状态不能为空")
    @Range(min = 0, max = 1, message = "非法自动关闭状态")
    private Integer autoCloseState;
}
