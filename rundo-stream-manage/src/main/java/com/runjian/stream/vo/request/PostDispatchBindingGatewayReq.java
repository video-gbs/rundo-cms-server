package com.runjian.stream.vo.request;

import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.validator.ValidationResult;
import com.runjian.common.validator.ValidatorFunction;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * 调度服务绑定网关
 * @author Miracle
 * @date 2023/2/9 11:26
 */
@Data
public class PostDispatchBindingGatewayReq implements ValidatorFunction {

    /**
     * 调度服务id
     */
    @NotNull(message = "调度服务id为空")
    @Range(min = 1, message = "非法调度服务id")
    private Long dispatchId;

    /**
     * 网关id组合
     */
    private Set<Long> gatewayIds;

    @Override
    public void validEvent(ValidationResult result, Object data, Object matchData) throws BusinessException {
        if (Objects.isNull(gatewayIds)){
            gatewayIds = Collections.EMPTY_SET;
        }
    }
}
