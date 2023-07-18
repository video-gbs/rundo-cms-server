package com.runjian.device.expansion.vo.feign.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 资源兄弟节点移动请求体
 * @author Miracle
 * @date 2023/6/9 14:42
 */
@Data
public class PutResourceBtMoveReq {

    /**
     * 部门id
     */
    @NotNull(message = "资源id不能为空")
    @Min(value = 1, message = "非法资源id")
    private Long id;

    /**
     * 移动指令，0-下移 1-上移
     */
    @NotNull(message = "移动指令不能为空")
    @Range(min = 0, max = 1, message = "非法移动指令")
    private Integer moveOp;
}
