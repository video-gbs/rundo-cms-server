package com.runjian.device.expansion.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * ptz/设备控制操作请求指令
 * @author chenjialing
 */
@Data
@ApiModel(value = "预置位操作指令")
public class ChannelPresetEditReq {


    @NotNull(message = "通道id不能为空")
    private Long channelExpansionId;

    @ApiModelProperty("预置位编码;0-255:未传递presetId时，为预置位增加")
//    @Range(min = 0,max = 255,message = "预置位编码范围：0-255")
    private Integer presetId;


    @ApiModelProperty("预置位名称")
    private String presetName;



}
