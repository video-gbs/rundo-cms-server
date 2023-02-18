package com.runjian.device.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/2/10 16:14
 */
@Data
public class PutGatewayReq {

    /**
     * 网关id
     */
    @NotNull(message = "网关id不能为空")
    @Range(min = 1, message = "非法网关id")
    private Long gatewayId;

    /**
     * 名称
     */
    @NotBlank(message = "网关名称不能为空")
    private String name;
}
