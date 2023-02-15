package com.runjian.stream.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/2/13 14:40
 */
@Data
public class PutStreamCloseReq {

    /**
     * 流id
     */
    @NotBlank(message = "流id不能为空")
    private String streamId;

    /**
     * 是否异常关闭
     */
    @NotNull(message = "是否可以关闭")
    private Boolean canClose;
}
