package com.runjian.cascade.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/12/22 9:28
 */
@Data
public class ChannelInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 平台id
     */
    private Long platformId;

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 节点id
     */
    private Long nodeId;

    /**
     * 名称
     */
    private String name;

    /**
     * 国标编码
     */
    private String gbCode;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
