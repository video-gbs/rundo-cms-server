package com.runjian.device.expansion.vo.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 修改资源请求体
 * @author Miracle
 * @date 2023/6/9 17:11
 */
@Data
public class PutVideoAreaReq {


    /**
     * 资源key
     */
    @NotNull(message = "资源key不能为空")
    private String resourceKey;
    /**
     * 资源id
     */
    @NotNull(message = "资源id不能为空")
    @Min(value = 1, message = "非法资源id")
    private Long resourceId;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空")
    @Size(max = 100, message = "资源名称的范围在1~100")
    private String resourceName;
}
