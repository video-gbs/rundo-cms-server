package com.runjian.stream.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 心跳信息
 * @author Miracle
 * @date 2023/2/3 17:51
 */
@Data
public class PutHeartbeatReq {

    /**
     * 调度服务ID
     */
    @NotNull(message = "调度服务不能为空")
    @Range(min = 1, message = "非法调度服务ID")
    private Long dispatchId;

    /**
     * 超时时间
     */
    @NotNull(message = "超时时间不能为空")
    private LocalDateTime outTime;
}
