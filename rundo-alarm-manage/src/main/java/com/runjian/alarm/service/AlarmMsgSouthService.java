package com.runjian.alarm.service;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/9/11 16:37
 */
public interface AlarmMsgSouthService {

    /**
     * 接收告警信息
     * @param channelId
     * @param eventCode
     * @param eventDesc
     * @param eventTime
     */
    void receiveAlarmMsg(Long channelId, String eventCode, Integer eventEndType, String eventDesc, LocalDateTime eventTime);



}
