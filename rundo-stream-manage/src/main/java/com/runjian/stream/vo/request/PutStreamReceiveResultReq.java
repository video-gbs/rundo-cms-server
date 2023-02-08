package com.runjian.stream.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/2/7 20:23
 */
@Data
public class PutStreamReceiveResultReq {

    /**
     * 流id
     */
    @NotBlank(message = "流id不能为空")
    private String streamId;

    /**
     * 是否播放成功
     */
    @NotNull(message = "是否播放成功不能为空")
    private Boolean isSuccess;
}
