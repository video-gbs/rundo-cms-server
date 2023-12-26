package com.runjian.cascade.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/12/22 9:55
 */
@Data
public class NodeInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 平台id
     */
    private Long platformId;

    /**
     * 节点父id
     */
    private Long nodePid;

    /**
     * 层级
     */
    private String level;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 国标编码
     */
    private String gbCode;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
