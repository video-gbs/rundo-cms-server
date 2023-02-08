package com.runjian.parsing.vo.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/2/7 18:12
 */
@Data
public class PutDispatchHeartbeatReq {

    /**
     * 网关序列号
     */
    private Long dispatchId;

    /**
     * 心跳时间
     */
    private LocalDateTime outTime;
}
