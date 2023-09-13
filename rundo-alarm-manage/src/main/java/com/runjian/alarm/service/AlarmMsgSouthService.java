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
     * @param channelId 通道id
     * @param eventCode 事件编码
     * @param eventMsgTypeCode 事件消息类型编码
     * @param eventDesc 事件描述
     * @param eventTime 事件时间
     * @param hasEventPhoto 是否有事件照片
     */
    void receiveAlarmMsg(Long channelId, String eventCode, Integer eventMsgTypeCode, String eventDesc, LocalDateTime eventTime, Integer hasEventPhoto);

    /**
     * 定时任务
     * 检测正在事件中的告警
     */
    void checkUnderwayAlarm();


    /**
     * 告警数据存储
     * @param alarmMsgId 告警信息id
     * @param alarmDataType 告警信息类型
     * @param file 文件
     */
    void saveAlarmFile(Long alarmMsgId, Integer alarmDataType, MultipartFile file);
}
