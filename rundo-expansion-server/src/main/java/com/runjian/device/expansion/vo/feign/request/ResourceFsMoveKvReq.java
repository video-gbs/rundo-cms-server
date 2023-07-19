package com.runjian.device.expansion.vo.feign.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 资源父子移动请求体
 * @author Miracle
 * @date 2023/6/9 14:38
 */
@Data
public class ResourceFsMoveKvReq {

    /**
     * 资源key【必须】
     */
    private String resourceKey;

    /**
     * 资源value【必须】
     */
    private String resourceValue;

    /**
     * 资源value【必须】
     */
    private String pResourceValue;
}
