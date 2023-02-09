package com.runjian.stream.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 网关绑定流媒体服务请求体
 * @author Miracle
 * @date 2023/2/9 11:21
 */
@Data
public class PostGatewayBindingDispatchReq {

    /**
     * 网关id
     */
    @NotNull(message = "网关id不能为空")
    @Range(min = 1, message = "非法网关id")
    private Long gatewayId;

    /**
     * 调度服务id
     */
    @Range(min = 1, message = "非法调度服务id")
    private Long dispatchId;
}
