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
public class PutResourceFsMoveReq {

    /**
     * 资源id
     */
    @NotNull(message = "资源id不能为空")
    @Min(value = 1, message = "非法资源id")
    private Long id;

    /**
     * 资源父id
     */
    @NotNull(message = "资源父id不能为空")
    @Min(value = 0, message = "非法资源父id")
    private Long resourcePid;
}
