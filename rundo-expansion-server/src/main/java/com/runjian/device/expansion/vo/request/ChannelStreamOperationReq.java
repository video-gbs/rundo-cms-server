package com.runjian.device.expansion.vo.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/2/7 20:46
 */
@Data
public class ChannelStreamOperationReq {

    /**
     * 流id
     */
    @ApiModelProperty("流id")
    @NotNull(message = "流id不能为空")
    private String streamId;

    /**
     * 通道id
     */
    @ApiModelProperty("通道id")
    @NotNull(message = "通道id不能为空")
    private Long channelId;


}
