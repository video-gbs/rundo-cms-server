package com.runjian.device.expansion.vo.feign.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Miracle
 * @date 2023/2/7 20:46
 */
@Data
public class PutStreamOperationReq{

    /**
     * 流id
     */
    @NotBlank(message = "流Id不能为空")
    private String streamId;

    /**
     * 通道id
     */
    private Long channelId;


}
