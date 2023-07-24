package com.runjian.device.expansion.vo.feign.request;

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
public class PutResourceReq {

    /**
     * 资源key
     */
    @NotNull(message = "资源key不能为空")
    private String resourceKey;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空")
    @Size(max = 100, message = "资源名称的范围在1~100")
    private String resourceName;

    /**
     * 资源value
     */
    @NotBlank(message = "资源value不能为空")
    @Size(max = 50, message = "资源value的范围在1~50")
    private String resourceValue;
}
