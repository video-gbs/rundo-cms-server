package com.runjian.alarm.service;

/**
 * @author Miracle
 * @date 2023/9/14 15:47
 */
public interface AlarmEventHandleService {

    /**
     * 定时任务
     * 检测正在事件中的告警
     */
    void checkUnderwayAlarm();

    /**
     * 下载告警录像任务处理
     */
    void alarmVideoEventStart();

    /**
     * 下载告警图片任务处理
     */
    void alarmImageEventStart();

    /**
     * 超时任务处理
     */
    void alarmEventCheck();

}
