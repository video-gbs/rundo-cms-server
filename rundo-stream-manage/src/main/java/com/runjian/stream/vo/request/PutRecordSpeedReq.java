package com.runjian.stream.vo.request;

import lombok.Data;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/3/28 10:52
 */
@Data
public class PutRecordSpeedReq extends PutStreamOperationReq {

    @NotNull(message = "播放速度不能为空")
    @DecimalMax(value = "4.0", message = "非法最大速度")
    @DecimalMin(value = "0.25", message = "非法最小速度")
    private Float speed;

}
