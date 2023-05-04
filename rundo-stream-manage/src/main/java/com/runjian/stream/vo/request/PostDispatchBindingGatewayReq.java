package com.runjian.stream.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 调度服务绑定网关
 * @author Miracle
 * @date 2023/2/9 11:26
 */
@Data
public class PostDispatchBindingGatewayReq {

    /**
     * 调度服务id
     */
    @NotNull(message = "调度服务id为空")
    @Range(min = 1, message = "非法调度服务id")
    private Long dispatchId;

    /**
     * 网关id组合
     */
    @NotNull(message = "网关选择不能为空")
    @Size(min = 1, message = "网关选择不能为空")
    private Set<Long> gatewayIds;

}
