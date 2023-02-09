package com.runjian.stream.vo.request;

import com.runjian.common.constant.PlayType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/2/7 20:39
 */
@Data
public class PostStreamApplyStreamReq {

    /**
     * 网关id不能为空
     */
    @NotNull(message = "网关id不能为空")
    @Range(min = 1, message = "非法网关id")
    private Long gatewayId;


    /**
     * 通道id不能为空
     */
    @NotNull(message = "通道id不能为空")
    @Range(min = 1, message = "非法通道id")
    private Long channelId;

    /**
     * 播放模式 {@link PlayType}
     */
    @NotNull(message = "播放模式不能为空")
    @Range( min = 1, max = 4, message = "非法播放模式")
    private Integer playType;

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
