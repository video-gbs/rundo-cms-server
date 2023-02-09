package com.runjian.stream.vo.response;

import lombok.Data;

@Data
public class PostApplyStreamRsp {

    /**
     * 调度服务地址
     */
    private String dispatchUrl;

    /**
     * 流id
     */
    private String streamId;

    /**
     * 录像状态
     */
    private Integer recordState;

}
