package com.runjian.device.expansion.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
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
    @NotNull(message = "父节点id不能为空")
    @Min(value = 0, message = "非法父节点")
    private Long resourcePid;

    /**
     * 资源Map resourceValue:resourceName
     */
    @NotNull(message = "安防通道名称不能为空")
    private String name;
}
