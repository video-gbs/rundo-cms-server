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
     * 流模式 默认不传的情况下为udp {@link  com.runjian.device.constant.StreamType}
     */
    @Range(min = 1, max = 2, message = "非法流模式")
    private Integer streamType;

    /**
     * 流是否开启录像
     */
    @Range(min = 0, max = 1, message = "非法录像状态")
    @NotNull(message = "录像状态不能为空")
    private Integer recordState;

    /**
     * 流是否无人观看自动关闭
     */
    @Range(min = 0, max = 1, message = "非法自动关闭状态")
    @NotNull(message = "自动关闭状态不能为空")
    private Integer autoCloseState;

}
