package com.runjian.parsing.vo.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PutGatewayHeartbeatReq {

    /**
     * 网关序列号
     */
    private Long gatewayId;

    /**
     * 心跳时间
     */
    private LocalDateTime outTime;

}
