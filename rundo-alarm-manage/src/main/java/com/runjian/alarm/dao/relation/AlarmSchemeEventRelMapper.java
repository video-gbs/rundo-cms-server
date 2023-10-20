package com.runjian.alarm.dao.relation;

import com.runjian.alarm.entity.relation.AlarmSchemeEventRel;
import com.runjian.alarm.vo.response.GetAlarmSchemeEventNameRsp;
import com.runjian.alarm.vo.response.GetAlarmSchemeEventRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/9/8 17:49
 */
@Mapper
@Repository
public interface AlarmSchemeEventRelMapper {

    String ALARM_SCHEME_EVENT_TABLE_NAME = "rundo_alarm_scheme_event";

    @Select(" <script> " +
            " SELECT scheme_id, event_name FROM " + ALARM_SCHEME_EVENT_TABLE_NAME +
            " WHERE scheme_id IN " +
            " <foreach collection='schemeIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    List<GetAlarmSchemeEventNameRsp> selectEventNameBySchemeIds(Set<Long> schemeIds);

    @Select(" SELECT * FROM " + ALARM_SCHEME_EVENT_TABLE_NAME +
            " WHERE scheme_id = #{schemeId}")
    List<GetAlarmSchemeEventRsp> selectRspBySchemeId(Long schemeId);

    @Insert(" <script> " +
            " INSERT INTO " + ALARM_SCHEME_EVENT_TABLE_NAME + " (scheme_id, event_code, event_level, event_interval, enable_video, video_length, video_has_audio, enable_photo, update_time, create_time) values " +
            " <foreach collection='alarmSchemeEventRelList' item='item' separator=','>(#{schemeId}, #{item.eventCode}, #{item.eventLevel}, #{item.eventInterval}, #{item.enableVideo}, #{item.videoLength}, #{item.videoHasAudio}, #{item.enablePhoto}, #{nowTime}, #{nowTime})</foreach> " +
            " </script>")
    void batchSaveBySchemeId(Long schemeId, List<AlarmSchemeEventRel> alarmSchemeEventRelList, LocalDateTime nowTime);

    @Insert(" <script> " +
            " INSERT INTO " + ALARM_SCHEME_EVENT_TABLE_NAME + " (scheme_id, event_code, event_level, event_interval, enable_video, video_length, video_has_audio, enable_photo, update_time, create_time) values " +
            " <foreach collection='channelIds' item='item' separator=','>(#{item.schemeId}, #{item.eventCode}, #{item.eventLevel}, #{item.eventInterval}, #{item.enableVideo}, #{item.videoLength}, #{item.videoHasAudio}, #{item.enablePhoto}, #{item.updateTime}, #{item.createTime})</foreach> " +
            " </script>")
    void batchSave(List<AlarmSchemeEventRel> alarmSchemeEventRelList);

    @Update(" <script> " +
            " <foreach collection='alarmSchemeEventRelList' item='item' separator=';'> " +
            " UPDATE " + ALARM_SCHEME_EVENT_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , event_level = #{item.eventLevel} " +
            " , event_interval = #{item.eventInterval} " +
            " , enable_video = #{item.enableVideo} " +
            " , video_length = #{item.videoLength} " +
            " , video_has_audio = #{item.videoHasAudio} " +
            " , enable_photo = #{item.enablePhoto} " +
            " WHERE id = #{item.eventCode} "+
            " </foreach> " +
            " </script> ")
    void batchUpdate(List<AlarmSchemeEventRel> alarmSchemeEventRelList);

    @Select(" SELECT event_code FROM " + ALARM_SCHEME_EVENT_TABLE_NAME +
            " WHERE scheme_id = #{schemeId}")
    Set<String> selectEventCodeBySchemeId(Long schemeId);

    @Delete(" <script> " +
            " DELETE FROM " + ALARM_SCHEME_EVENT_TABLE_NAME +
            " WHERE scheme_id = #{schemeId} " +
            " AND event_code IN " +
            " <foreach collection='eventCodeList' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    void batchDeleteBySchemeIdAndEventCodes(Long schemeId, List<String> eventCodeList);

    @Delete(" DELETE FROM " + ALARM_SCHEME_EVENT_TABLE_NAME +
            " WHERE scheme_id = #{schemeId} ")
    void deleteBySchemeId(Long schemeId);

    @Select(" SELECT * FROM " + ALARM_SCHEME_EVENT_TABLE_NAME +
            " WHERE scheme_id = #{schemeId} " +
            " AND event_code = #{eventCode} ")
    Optional<AlarmSchemeEventRel> selectBySchemeIdAndEventCode(Long schemeId, String eventCode);
}
