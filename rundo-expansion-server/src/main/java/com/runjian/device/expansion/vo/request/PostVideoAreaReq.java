package com.runjian.device.expansion.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class PostVideoAreaReq {

    /**
     * 父节点id
     */
    private Long resourcePid;

    /**
     * 资源key
     */
    @NotBlank(message = "资源key不能为空")
    private String resourceKey;
    /**
     * 父节点id
     */
    @NotNull(message = "父节点value不能为空")
    @JsonProperty(value = "pResourceValue")
    private Long pResourceValue;

    /**
     * 资源Map resourceValue:resourceName
     */
    @NotNull(message = "安防通道名称不能为空")
    private String name;
}
