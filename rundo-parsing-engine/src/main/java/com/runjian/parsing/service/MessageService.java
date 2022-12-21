package com.runjian.parsing.service;

public interface MessageService {

    /**
     * 消息分配
     * @param msgType
     * @param msg
     */
    void msgDispatch(String msgType, Object msg);

}
