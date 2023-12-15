package com.runjian.device.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/12/15 14:56
 */
@Data
public class GetNodeRsp {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 母节点id
     */
    private String parentId;

    /**
     * 原始id
     */
    private String originId;

    /**
     * 节点名称
     */
    private String nodeName;
}
