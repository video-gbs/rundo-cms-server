package com.runjian.device.expansion.vo.feign.request;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("资源key，区分设备和通道资源")
    private String resourceKey;

    /**
     * 资源value【必须】
     */
    @ApiModelProperty("资源value,后端返回的resourceValue")
    private String resourceValue;

    /**
     * 资源value【必须】
     */
    @ApiModelProperty("父级资源value,后端返回的上一级的resourceValue")
    private String pResourceValue;
}
