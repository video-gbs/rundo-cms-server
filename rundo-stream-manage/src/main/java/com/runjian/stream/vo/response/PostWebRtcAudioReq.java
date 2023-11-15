package com.runjian.stream.vo.response;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * webRTC音频传输录制请求体
 * @author Miracle
 * @date 2023/10/23 15:55
 */
@Data
public class PostWebRtcAudioReq {

    /**
     * 通道id
     */
    @NotNull(message = "通道id不能为空")
    @Range(min = 1, message = "非法通道id")
    private Long channelId;

    /**
     * 录制状态
     */
    @NotNull(message = "录制状态不能为空")
    @Range(min = 0, max = 1, message = "非法录制状态")
    private Integer recordState;

    /**
     * 是否自动关闭
     */
    @NotNull(message = "是否自动关闭流不能为空")
    @Range(min = 0, max = 1, message = "非法自动关闭状态")
    private Integer autoCloseState;
}
