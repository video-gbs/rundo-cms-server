package com.runjian.parsing.vo.feign;

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
    private String streamId;

    /**
     * 是否播放成功
     */
    private Boolean isSuccess;
}
