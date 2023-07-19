package com.runjian.device.expansion.vo.feign.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 资源兄弟节点移动请求体
 * @author Miracle
 * @date 2023/6/9 14:42
 */
@Data
public class PutResourceBtMoveReq {
    /**
     * 资源key
     */
    @NotNull(message = "资源key不能为空")
    private String resourceKey;

    /**
     * 资源value
     */
    @NotBlank(message = "资源value不能为空")
    @Size(max = 50, message = "资源value的范围在1~50")
    private String resourceValue;

    /**
     * 移动指令，0-下移 1-上移
     */
    @NotNull(message = "移动指令不能为空")
    @Range(min = 0, max = 1, message = "非法移动指令")
    private Integer moveOp;
}
