package com.runjian.stream.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/2/3 10:12
 */
@Data
public class GatewayDispatchInfo {


    /**
     * 主键id
     */
    private Long id;

    /**
     * 网关id
     */
    private Long gatewayId;

    /**
     * 调度服务id
     */
    private Long dispatchId;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;


}
