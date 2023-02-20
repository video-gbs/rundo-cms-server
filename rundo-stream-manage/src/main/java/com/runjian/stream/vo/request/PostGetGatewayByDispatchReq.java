package com.runjian.stream.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/2/20 20:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostGetGatewayByDispatchReq {

    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer num;

    /**
     * 网关id数组
     */
    private List<Long> gatewayIds;

    /**
     * 是否包含
     */
    private Boolean isIn;

    /**
     * 网关名称
     */
    private String name;
}
