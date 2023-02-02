package com.runjian.stream.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 调度服务信息
 * @author Miracle
 * @date 2023/2/2 15:43
 */
@Data
public class DispatchInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 序列号
     */
    private String serialNum;

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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
