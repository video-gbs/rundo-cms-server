package com.runjian.alarm.service;

import org.springframework.web.multipart.MultipartFile;

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


    /**
     * 告警数据存储
     * @param alarmMsgId 告警信息id
     * @param alarmDataType 告警信息类型
     * @param file 文件
     */
    void saveAlarmFile(Long alarmMsgId, Integer alarmDataType, MultipartFile file);
}
