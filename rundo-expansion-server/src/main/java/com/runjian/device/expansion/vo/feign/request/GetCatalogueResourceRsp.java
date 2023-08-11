package com.runjian.device.expansion.vo.feign.request;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/7/17 20:00
 */
@Data
public class GetCatalogueResourceRsp {

    /**
     * 资源value
     */
    private String resourceValue;

    /**
     * 资源id
     */
    private Long resourceId;

    /**
     * 层级名称
     */
    private String levelName;

    /**
     * 资源名称
     */
    private String resourceName;
}
