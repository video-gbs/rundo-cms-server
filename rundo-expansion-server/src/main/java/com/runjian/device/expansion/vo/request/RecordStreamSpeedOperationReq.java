package com.runjian.device.expansion.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/2/7 20:46
 */
@Data
public class RecordStreamSpeedOperationReq extends RecordStreamOperationReq{

    @NotNull(message = "播放速度不能为空")
    @DecimalMax(value = "4.0", message = "非法最大速度")
    @DecimalMin(value = "0.25", message = "非法最小速度")
    @ApiModelProperty("播放速度")
    private Float speed;


}
