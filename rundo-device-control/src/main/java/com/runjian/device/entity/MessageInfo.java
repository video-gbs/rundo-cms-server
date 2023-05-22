package com.runjian.device.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/5/16 14:26
 */
@Data
public class MessageInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 消息订阅名称
     */
    private String msgHandle;

    /**
     * 消息锁
     */
    private String msgLock;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
