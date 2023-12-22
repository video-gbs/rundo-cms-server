package com.runjian.cascade.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/12/22 15:18
 */
@Data
public class GetPlatformPageRsp {

    /**
     * 平台id
     */
    private Long id;

    /**
     * 平台名称
     */
    private String name;

    /**
     * 平台ip
     */
    private String ip;

    /**
     * 平台端口
     */
    private Integer port;

    /**
     * 国标编码
     */
    private String gbCode;

    /**
     * 上下线状态
     */
    private Integer onlineState;

    /**
     * 注册状态
     */
    private Integer signState;
}
