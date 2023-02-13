package com.runjian.stream.vo.request;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/2/13 14:40
 */
@Data
public class PutStreamCloseReq {

    /**
     * 流id
     */
    private String streamId;

    /**
     * 是否异常关闭
     */
    private Boolean canClose;
}
