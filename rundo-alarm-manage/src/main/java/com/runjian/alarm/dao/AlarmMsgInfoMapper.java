package com.runjian.alarm.dao;

import com.runjian.alarm.vo.response.GetAlarmMsgRsp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/9/8 17:49
 */
@Mapper
@Repository
public interface AlarmMsgInfoMapper {

    String ALARM_MSG_TABLE_NAME = "rundo_alarm_msg";

    void deleteById(Long id);

    List<GetAlarmMsgRsp> selectByAlarmTypeAndAlarmTime(String alarmType, LocalDateTime alarmStartTime, LocalDateTime alarmEndTime);

}
