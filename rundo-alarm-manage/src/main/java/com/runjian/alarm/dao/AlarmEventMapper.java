package com.runjian.alarm.dao;

import cn.hutool.core.lang.Opt;
import com.runjian.alarm.entity.AlarmEventInfo;
import com.runjian.alarm.vo.response.GetAlarmEventNameRsp;
import com.runjian.alarm.vo.response.GetAlarmEventRsp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/9/8 17:49
 */
@Mapper
@Repository
public interface AlarmEventMapper {

    String ALARM_EVENT_TABLE_NAME = "rundo_alarm_event";

    List<GetAlarmEventRsp> getAlarmEventByPage(String eventName, String eventCode);

    List<AlarmEventInfo> selectByEventNameOrEventCode(String eventName, String eventCode);

    void save(AlarmEventInfo alarmEventInfo);

    Optional<AlarmEventInfo> selectById(Long id);

    Optional<AlarmEventInfo> selectByEventName(String eventName);

    void update(AlarmEventInfo alarmEventInfo);

    void deleteById(Long id);

    List<GetAlarmEventNameRsp> selectEventName();
}
