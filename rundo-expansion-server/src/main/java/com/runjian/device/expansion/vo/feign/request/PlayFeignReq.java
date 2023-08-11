package com.runjian.device.expansion.vo.feign.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author chenjialing
 */
@Data
public class PlayFeignReq {
    @ApiModelProperty("通道id")
    @NotNull(message = "通道id不能为空")
    @Range(min = 1, message = "非法通道id")
    private Integer channelId;

    @ApiModelProperty("是否开启音频,0不开启，1开启")
    private Integer enableAudio = 0;

    @ApiModelProperty("是否开启ssrc检测，false不开启，true开启")
    private Boolean ssrcCheck = true;

    @ApiModelProperty("推流模式，1UDP，2TCP")
    private Integer streamType = 2;

    private Integer playType = 2;

    @ApiModelProperty("录像状态，0关，1开")
    private Integer recordState = 0;

    @ApiModelProperty("无人观看是否开启，0关，1开")
    private Integer autoCloseState = 1;

    /**
     * 码流id(主子码流)
     */
    @NotNull(message = "主子码流选择不能为空")
    @Range(min = 0, message = "非法主子码流")
    private Integer bitStreamId = 0;








}
