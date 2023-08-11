package com.runjian.device.expansion.vo.feign.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/2/6 9:29
 */
@Data
public class GetDispatchRsp {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 序列号
     */
    private String serialNum;

    /**
     * 在线状态 0离线 1在线
     */
    private Integer onlineState;

    /**
     * 名称
     */
    private String name;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * url地址
     */
    private String url;
}
