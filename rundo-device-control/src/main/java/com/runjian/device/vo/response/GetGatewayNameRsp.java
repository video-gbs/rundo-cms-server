package com.runjian.device.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/2/6 17:20
 */
@Data
public class GetGatewayNameRsp {

    /**
     * 网关id
     */
    private Long id;

    /**
     * 网关名称
     */
    private String name;
}
