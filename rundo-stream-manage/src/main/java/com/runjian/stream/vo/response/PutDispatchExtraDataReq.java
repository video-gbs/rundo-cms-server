package com.runjian.stream.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/2/7 20:06
 */
@Data
public class PutDispatchExtraDataReq {

    /**
     * 调度服务id
     */
    private Long dispatchId;

    /**
     * 调度服务名称
     */
    private String name;

    /**
     * 调度服务调用url
     */
    private String url;
}

