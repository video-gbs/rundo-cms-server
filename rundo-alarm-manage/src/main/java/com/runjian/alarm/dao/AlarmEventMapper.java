package com.runjian.alarm.dao;

import com.runjian.alarm.entity.AlarmEventInfo;
import com.runjian.alarm.vo.response.GetAlarmEventNameRsp;
import com.runjian.alarm.vo.response.GetAlarmEventRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/9/8 17:49
 */
@Mapper
@Repository
public interface AlarmEventMapper {

    String ALARM_EVENT_TABLE_NAME = "rundo_alarm_event";

    @Select(" <script> " +
            " SELECT * FROM " + ALARM_EVENT_TABLE_NAME +
            " WHERE 1=1 " +
            " <if test=\"eventName != null\" > AND event_name = #{eventName} </if> " +
            " <if test=\"eventCode != null\" > AND event_code = #{eventCode} </if> " +
            " </script> ")
    List<GetAlarmEventRsp> selectAlarmEventByPage(String eventName, String eventCode);

    @Select(" SELECT * FROM " + ALARM_EVENT_TABLE_NAME +
            " WHERE event_name = #{eventName} " +
            " OR event_code = #{eventCode} ")
    List<AlarmEventInfo> selectByEventNameOrEventCode(String eventName, String eventCode);

    @Insert(" INSERT INTO " + ALARM_EVENT_TABLE_NAME + "(event_name, event_code, event_sort, event_desc, update_time, create_time) values " +
            " (#{eventName}, #{eventCode}, #{eventSort}, #{eventDesc}, #{updateTime}, #{createTime}) " )
    void save(AlarmEventInfo alarmEventInfo);

    @Select(" SELECT * FROM " + ALARM_EVENT_TABLE_NAME +
            " WHERE id = #{id}")
    Optional<AlarmEventInfo> selectById(Long id);

    @Select(" SELECT * FROM " + ALARM_EVENT_TABLE_NAME +
            " WHERE event_name = #{eventName}")
    Optional<AlarmEventInfo> selectByEventName(String eventName);

    @Update(" <script> " +
            " UPDATE " + ALARM_EVENT_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , event_name = #{eventName} " +
            " , event_sort = #{eventSort} " +
            " , event_desc = #{eventDesc} " +
            " WHERE id = #{id} " +
            " </script> ")
    void update(AlarmEventInfo alarmEventInfo);

    @Delete(" DELETE FROM " + ALARM_EVENT_TABLE_NAME +
            " WHERE id = #{id} ")
    void deleteById(Long id);

    @Select(" <script> " +
            " SELECT id, event_name, event_sort, event_code FROM " + ALARM_EVENT_TABLE_NAME +
            " <if test=\"eventName != null\" > WHERE event_name LIKE CONCAT('%', #{eventName}, '%') </if> " +
            " </script> ")
    List<GetAlarmEventNameRsp> selectEventName(String eventName);

    @Select(" <script> " +
            " SELECT * FROM " + ALARM_EVENT_TABLE_NAME +
            " WHERE event_code IN " +
            " <foreach collection='eventCodes' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    List<AlarmEventInfo> selectByEventCodes(Set<String> eventCodes);
}
