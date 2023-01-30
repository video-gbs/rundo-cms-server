package com.runjian.parsing.vo.request;

import com.alibaba.druid.util.StringUtils;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.validator.ValidationResult;
import com.runjian.common.validator.ValidatorFunction;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 网关注册请求体
 * @author Miracle
 * @date 2023/1/12 9:43
 */
@Data
public class GatewaySignInReq implements ValidatorFunction {

    /**
     * 协议
     */
    @NotBlank(message = "协议类型不能为空")
    private String protocol;

    /**
     * 网关类型 {@link com.runjian.parsing.constant.GatewayType}
     */
    private String gatewayType;

    /**
     * ip
     */
    @NotBlank(message = "ip地址不能为空")
    private String ip;

    /**
     * 端口
     */
    @NotBlank(message = "端口不能为空")
    private String port;

    /**
     * 过期时间
     */
    @NotNull(message = "过期时间不能为空")
    private String outTime;

    @Override
    public void validEvent(ValidationResult result, Object data, Object matchData) throws BusinessException {
        if (!StringUtils.isNumber(outTime)){
            result.setHasErrors(true);
            result.getErrorMsgMap().put("请求参数有误","过期时间非时间戳");
            return;
        }
        if (System.currentTimeMillis() >= Long.parseLong(outTime)){
            result.setHasErrors(true);
            result.getErrorMsgMap().put("请求参数有误","过期时间是过去的时间");
        }
    }
}
