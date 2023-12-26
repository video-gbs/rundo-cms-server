package com.runjian.device.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/12/14 15:46
 */
@Data
public class NodeInfo {

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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
