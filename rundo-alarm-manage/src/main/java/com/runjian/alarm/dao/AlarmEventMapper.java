package com.runjian.alarm.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/9/8 17:49
 */
@Mapper
@Repository
public interface AlarmEventMapper {

    String ALARM_EVENT_TABLE_NAME = "rundo_alarm_event";
}
