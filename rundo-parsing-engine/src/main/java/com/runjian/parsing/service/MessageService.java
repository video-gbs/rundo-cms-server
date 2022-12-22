package com.runjian.parsing.service;

import org.springframework.amqp.core.Message;

public interface MessageService {

    /**
     * 消息分配
     * @param message
     */
    void msgDispatch(Message message);

}
