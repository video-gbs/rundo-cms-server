package com.runjian.stream.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/2/10 16:10
 */
@Data
public class GetDispatchNameRsp {

    /**
     * 调度服务id
     */
    private Long dispatchId;

    /**
     * 调度服务名称
     */
    private String name;
}
