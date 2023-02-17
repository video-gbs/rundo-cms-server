package com.runjian.device.expansion.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author chenjialing
 */
@Data
public class PlayReq {
    @ApiModelProperty("通道id")
    @NotNull(message = "通道id不能为空")
    @Range(min = 1, message = "非法通道id")
    private Integer channelId;

}
