package com.runjian.stream.vo.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/12/8 15:31
 */
@Data
public class PostStreamPushRunReq {

    /**
     * 推流信息
     */
    @NotNull(message = "推流信息id不能为空")
    @Min(value = 1, message = "推流信息id不能小于1")
    private Long streamPushId;
}
