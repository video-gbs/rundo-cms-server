package com.runjian.parsing.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 设备添加请求体
 * @author Miracle
 * @date 2023/1/9 15:11
 */
@Data
public class PostDeviceAddReq {

    /**
     * 设备ID
     */
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;

    /**
     * 网关ID
     */
    @NotNull(message = "网关ID不能为空")
    @Range(min = 1, message = "非法网关Id")
    private Long gatewayId;
}
