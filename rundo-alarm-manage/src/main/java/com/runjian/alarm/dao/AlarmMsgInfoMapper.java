package com.runjian.alarm.dao;

import com.runjian.alarm.entity.AlarmMsgInfo;
import com.runjian.alarm.vo.response.GetAlarmMsgRsp;
import org.apache.ibatis.annotations.*;
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

    @Delete(" DELETE FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE id = #{id} " )
    void deleteById(Long id);

    @Select(" <script> " +
            " SELECT * FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE 1=1 " +
            " <if test=\"alarmType != null\" > AND alarm_type = #{alarmType} </if>" +
            " <if test=\"alarmStartTime != null\" > AND alarm_time &gt;= #{alarmStartTime} </if>" +
            " <if test=\"alarmEndTime != null\" > AND alarm_time &lt;= #{alarmEndTime} </if>" +
            " </script> ")
    List<GetAlarmMsgRsp> selectByAlarmTypeAndAlarmTime(String alarmType, LocalDateTime alarmStartTime, LocalDateTime alarmEndTime);

    @Insert(" INSERT INTO " + ALARM_MSG_TABLE_NAME + " (channel_id, alarm_code, alarm_level, alarm_type, alarm_start_time, alarm_end_time, alarm_desc, alarm_state, alarm_interval, " +
            " video_url, video_length, video_audio_state, video_stream_id, " +
            "image_state, image_url, update_time, create_time) values " +
            " (#{channelId}, #{alarmCode}, #{alarmLevel}, #{alarmType}, #{alarmStartTime}, #{alarmEndTime}, #{alarmDesc}, #{alarmState}, #{alarmInterval}," +
            " #{videoUrl}, #{videoLength}, #{videoAudioState}, #{videoStreamId}, " +
            " #{imageState}, #{imageUrl}, #{updateTime}, #{createTime}) ")
    void save(AlarmMsgInfo alarmMsgInfo);

    @Select(" SELECT * FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE id = #{id} ")
    Optional<AlarmMsgInfo> selectById(Long alarmMsgId);

    @Update(" <script> " +
            " UPDATE " + ALARM_MSG_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " AND alarm_state = #{alarmState} " +
            " AND alarm_end_time = #{alarmEndTime} " +
            " AND video_url = #{videoUrl} " +
            " AND video_state = #{videoState} " +
            " AND video_stream_id = #{videoStreamId}" +
            " AND image_state = #{imageState} " +
            " AND image_url = #{imageUrl} " +
            " WHERE id = #{id} " +
            " </script> ")
    void update(AlarmMsgInfo alarmMsgInfo);

    @Select(" SELECT * FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE alarm_state = #{alarmState} ")
    List<AlarmMsgInfo> selectByAlarmState(Integer alarmState);

    @Update(" <script> " +
            " <foreach collection='alarmMsgInfoList' item='item' separator=';'> " +
            " UPDATE " + ALARM_MSG_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " AND alarm_state = #{item.alarmState} " +
            " AND alarm_end_time = #{item.alarmEndTime} " +
            " AND video_url = #{item.videoUrl} " +
            " AND video_state = #{item.videoState} " +
            " AND video_stream_id = #{item.videoStreamId}" +
            " AND image_state = #{item.imageState} " +
            " AND image_url = #{item.imageUrl} " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void batchUpdate(List<AlarmMsgInfo> alarmMsgInfoList);

    @Select(" SELECT * FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE video_state = #{alarmFileState} OR " +
            " image_state = #{alarmFileState} ")
    List<AlarmMsgInfo> selectByVideoStateAndAlarmEndTime(Integer alarmFileState, LocalDateTime nowTime);

    @Select(" SELECT * FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE video_state = #{alarmFileState} OR " +
            " image_state = #{alarmFileState} ")
    List<AlarmMsgInfo> selectByVideoStateOrImageState(Integer alarmFileState);

    @Select(" SELECT * FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE image_state = #{alarmFileState} ")
    List<AlarmMsgInfo> selectByImageState(Integer alarmFileState);
}
