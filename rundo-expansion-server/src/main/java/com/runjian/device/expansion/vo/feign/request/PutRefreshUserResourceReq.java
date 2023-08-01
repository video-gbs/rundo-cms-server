package com.runjian.device.expansion.vo.feign.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 刷新用户资源缓存请求体
 * @author Miracle
 * @date 2023/7/31 15:32
 */
@Data
public class PutRefreshUserResourceReq {

    @NotBlank(message = "资源key不能为空")
    @Size(min = 1, max = 999999, message = "非法资源Key")
    private String resourceKey;
}
