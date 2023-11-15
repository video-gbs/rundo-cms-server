package com.runjian.alarm.service;

/**
 * @author Miracle
 * @date 2023/11/8 11:10
 */
public interface AlarmSchemeChannelService {

    /**
     * 初始化
     */
    void init();

    /**
     * 检测被删除的通道
     */
    void checkDeleteChannel();
}
