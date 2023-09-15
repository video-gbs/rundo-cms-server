package com.runjian.alarm.dao;

import com.runjian.alarm.entity.AlarmMsgInfo;
import com.runjian.alarm.vo.response.GetAlarmMsgRsp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    void save(AlarmMsgInfo alarmMsgInfo);

    Optional<AlarmMsgInfo> selectById(Long alarmMsgId);

    void update(AlarmMsgInfo alarmMsgInfo);

    List<AlarmMsgInfo> selectByAlarmState(Integer alarmState);

    void batchUpdate(List<AlarmMsgInfo> alarmMsgInfoList);

    List<AlarmMsgInfo> selectByVideoStateAndAlarmEndTime(Integer alarmFileState, LocalDateTime nowTime);

    List<AlarmMsgInfo> selectByVideoStateOrImageState(Integer alarmFileState);

    List<AlarmMsgInfo> selectByImageState(Integer alarmFileState);
}
