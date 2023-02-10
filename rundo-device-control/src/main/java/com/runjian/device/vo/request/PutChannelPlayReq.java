package com.runjian.device.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 通道点播请求
 * @author Miracle
 * @date 2023/1/12 15:55
 */
@Data
public class PutChannelPlayReq {

    /**
     * 通道id
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
     * 流模式 默认不传的情况下为udp {@link  com.runjian.device.constant.StreamType}
     */
    @Size(min = 1, max = 2, message = "非法流模式")
    private Integer streamType;

}
