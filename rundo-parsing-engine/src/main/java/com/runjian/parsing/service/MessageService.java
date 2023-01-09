package com.runjian.parsing.service;

import com.runjian.parsing.vo.GatewayMqDto;
import org.springframework.amqp.core.Message;

public interface MessageService {

    /**
     * 消息分配
     * @param request
     */
    void msgDispatch(GatewayMqDto request);

}
