package com.runjian.alarm.service;

import com.github.pagehelper.PageInfo;
import com.runjian.alarm.vo.response.GetAlarmMsgRsp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 告警信息北向接口
 * @author Miracle
 * @date 2023/9/11 9:33
 */
public interface AlarmMsgNorthService {

    /**
     * 分页获取告警信息
     * @param page 页码
     * @param num 每页数据
     * @param alarmType 告警类型
     * @param alarmStartTime 告警开始时间
     * @param alarmEndTime 告警结束时间
     * @return
     */
    PageInfo<GetAlarmMsgRsp> getAlarmMsgByPage(int page, int num, String alarmType, LocalDateTime alarmStartTime, LocalDateTime alarmEndTime);

    /**
     * 删除告警信息
     * @param id 告警信息id
     */
    void deleteAlarmMsg(List<Long> idList);


}
