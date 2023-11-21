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

    @Select(" <script> " +
            " SELECT * FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE 1=1 " +
            " <if test=\"channelId != null\" > AND channel_id = #{channelId} </if>" +
            " <if test=\"alarmDesc != null\" > AND alarm_desc = #{alarmDesc} </if>" +
            " <if test=\"alarmStartTime != null\" > AND alarm_start_time &gt;= #{alarmStartTime} </if>" +
            " <if test=\"alarmEndTime != null\" > AND alarm_start_time &lt;= #{alarmEndTime} </if>" +
            " ORDER BY alarm_start_time DESC" +
            " </script> ")
    List<GetAlarmMsgRsp> selectByAlarmDescAndAlarmTime(Long channelId, String alarmDesc, LocalDateTime alarmStartTime, LocalDateTime alarmEndTime);

    @Insert(" INSERT INTO " + ALARM_MSG_TABLE_NAME + " (channel_id, alarm_code, alarm_level, alarm_start_time, alarm_end_time, alarm_desc, alarm_interval, " +
            " video_url, video_length, video_state, video_audio_state, video_stream_id, " +
            "image_state, image_url, update_time, create_time) values " +
            " (#{channelId}, #{alarmCode}, #{alarmLevel}, #{alarmStartTime}, #{alarmEndTime}, #{alarmDesc}, #{alarmInterval}," +
            " #{videoUrl}, #{videoLength}, #{videoState}, #{videoAudioState}, #{videoStreamId}, " +
            " #{imageState}, #{imageUrl}, #{updateTime}, #{createTime}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(AlarmMsgInfo alarmMsgInfo);

    @Select(" SELECT * FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE id = #{id} FOR UPDATE ")
    Optional<AlarmMsgInfo> selectById(Long alarmMsgId);

    @Update(" <script> " +
            " UPDATE " + ALARM_MSG_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , alarm_end_time = #{alarmEndTime} " +
            " <if test=\"videoUrl != null\" > , video_url = #{videoUrl} </if>" +
            " , video_state = #{videoState} " +
            " <if test=\"videoStreamId != null\" > , video_stream_id = #{videoStreamId} </if> " +
            " , image_state = #{imageState} " +
            " <if test=\"imageUrl != null\" > , image_url = #{imageUrl} </if>" +
            " WHERE id = #{id} " +
            " </script> ")
    void update(AlarmMsgInfo alarmMsgInfo);

    @Update(" <script> " +
            " <foreach collection='alarmMsgInfoList' item='item' separator=';'> " +
            " UPDATE " + ALARM_MSG_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , alarm_end_time = #{item.alarmEndTime} " +
            " <if test=\"item.videoUrl != null\" > , video_url = #{item.videoUrl} </if> " +
            " , video_state = #{item.videoState} " +
            " <if test=\"item.videoStreamId != null\" > , video_stream_id = #{item.videoStreamId} </if> " +
            " , image_state = #{item.imageState} " +
            " <if test=\"item.imageUrl != null\" > , image_url = #{item.imageUrl} </if>" +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void batchUpdate(List<AlarmMsgInfo> alarmMsgInfoList);

    @Select(" <script> " +
            " SELECT * FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE video_state = #{alarmFileState} " +
            " AND alarm_end_time &lt;= #{nowTime} " +
            " </script> ")
    List<AlarmMsgInfo> selectByVideoStateAndAlarmEndTime(Integer alarmFileState, LocalDateTime nowTime);

    @Select(" SELECT * FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE video_state = #{alarmFileState} OR " +
            " image_state = #{alarmFileState} ")
    List<AlarmMsgInfo> selectByVideoStateOrImageState(Integer alarmFileState);

    @Select(" <script> " +
            " SELECT * FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE image_state = #{alarmFileState} " +
            " AND alarm_start_time &lt;= #{nowTime} " +
            " </script> ")
    List<AlarmMsgInfo> selectByImageStateAndAlarmEndTime(Integer alarmFileState, LocalDateTime nowTime);


    @Delete(" <script> " +
            " DELETE FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE id IN " +
            " <foreach collection='idList' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    void deleteByIdList(List<Long> idList);

    @Select(" <script> " +
            " SELECT * FROM " + ALARM_MSG_TABLE_NAME +
            " WHERE video_state = #{alarmFileState} " +
            " </script> ")
    List<AlarmMsgInfo> selectByVideoState(Integer alarmFileState);
}
