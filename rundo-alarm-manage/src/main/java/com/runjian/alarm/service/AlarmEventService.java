package com.runjian.alarm.service;

import com.github.pagehelper.PageInfo;
import com.runjian.alarm.vo.response.GetAlarmEventNameRsp;
import com.runjian.alarm.vo.response.GetAlarmEventRsp;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/9/11 9:25
 */
public interface AlarmEventService {

    /**
     * 分页获取事件
     * @param page 页码
     * @param num 每页数量
     * @param eventName 事件名称
     * @param eventCode 事件编码
     * @return
     */
    PageInfo<GetAlarmEventRsp> getAlarmEventByPage(int page, int num, String eventName, String eventCode);

    /**
     * 获取事件名称
     * @return
     */
    List<GetAlarmEventNameRsp> getAlarmEventName(String eventName);

    /**
     * 添加事件
     * @param eventName 事件名称
     * @param eventCode 事件编码
     * @param eventDesc 事件描述
     */
    void addAlarmEvent(String eventName, String eventCode, Integer eventSort, String eventDesc);

    /**
     * 修改事件
     * @param id 主键id
     * @param eventName 事件名称
     * @param eventDesc 事件描述
     */
    void updateAlarmEvent(Long id, String eventName, Integer eventSort, String eventDesc);

    /**
     * 删除事件
     * @param id
     */
    void deleteAlarmEvent(Long id);
}
