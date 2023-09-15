package com.runjian.alarm.dao.relation;

import com.runjian.alarm.entity.relation.AlarmMsgErrorRel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Miracle
 * @date 2023/9/15 15:52
 */
@Mapper
@Repository
public interface AlarmMsgErrorRelMapper {

    String ALARM_MSG_ERROR_TABLE_NAME = "rundo_alarm_msg_error";

    void save(AlarmMsgErrorRel alarmMsgErrorRel);
}
