package com.runjian.device.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miracle
 * @date 2023/5/16 15:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMessageSubRsp {

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 消息订阅名称
     */
    private String msgHandle;

    /**
     * 消息锁
     */
    private String msgLock;
}
