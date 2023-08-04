package com.runjian.device.expansion.vo.feign.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * 批量添加资源请求体
 * @author Miracle
 * @date 2023/6/9 17:05
 */
@Data
public class PostBatchResourceKvReq {



    /**
     * 资源key
     */
    @NotBlank(message = "资源key不能为空")
    private String resourceKey;

    /**
     * 资源value
     */
    @NotBlank(message = "父资源value不能为空")
    private String pResourceValue;

    /**
     * 资源类型 1-目录 2-资源
     */
    @NotNull(message = "资源类型不能为空")
    @Range(min = 1, max = 2, message = "非法资源类型")
    private Integer resourceType;

    /**
     * 资源Map resourceValue:resourceName
     */
    @NotNull(message = "资源value不能为空")
    @Size(min = 1, message = "资源value不能为空")
    private Map<String,String> resourceMap;
}
