package com.runjian.device.expansion.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 网关心跳请求体
 * @author Miracle
 * @date 2023/1/11 10:05
 */
@Data
public class PutHeartbeatReq {

    /**
     * 网关ID
     */
    @NotNull(message = "网关ID不能为空")
    @Range(min = 1, message = "非法网关ID")
    private Long gatewayId;

    /**
     * 超时时间
     */
    @NotNull(message = "超时时间不能为空")
    private LocalDateTime outTime;
}
