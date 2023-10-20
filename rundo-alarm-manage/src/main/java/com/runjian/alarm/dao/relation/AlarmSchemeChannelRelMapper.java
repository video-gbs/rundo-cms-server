package com.runjian.alarm.dao.relation;

import com.runjian.alarm.dao.AlarmSchemeInfoMapper;
import com.runjian.alarm.entity.relation.AlarmSchemeChannelRel;
import com.runjian.alarm.vo.response.GetAlarmChannelDeployRsp;
import com.runjian.alarm.vo.response.GetAlarmChannelRsp;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/9/8 17:49
 */
@Mapper
@Repository
public interface AlarmSchemeChannelRelMapper {

    String ALARM_SCHEME_CHANNEL_TABLE_NAME = "rundo_alarm_scheme_channel";

    @Select(" SELECT channel_id FROM " + ALARM_SCHEME_CHANNEL_TABLE_NAME +
            " WHERE scheme_id = #{schemeId} ")
    List<Long> selectChannelIdBySchemeId(Long schemeId);

    @Select(" <script> " +
            " SELECT asct.channel_id, ast.scheme_name, ast.id AS schemeId FROM " + AlarmSchemeInfoMapper.ALARM_SCHEME_TABLE_NAME + " ast " +
            " LEFT JOIN " + ALARM_SCHEME_CHANNEL_TABLE_NAME + " asct ON ast.id = asct.scheme_id " +
            " WHERE asct.channel_id IN " +
            " <foreach collection='channelIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    List<GetAlarmChannelRsp> selectSchemeNameByChannelIds(Set<Long> channelIds);

    @Insert(" <script> " +
            " INSERT INTO " + ALARM_SCHEME_CHANNEL_TABLE_NAME + " (scheme_id, channel_id, create_time, update_time) values " +
            " <foreach collection='channelIds' item='item' separator=','>(#{schemeId}, #{item}, #{nowTime}, #{nowTime})</foreach> " +
            " </script>")
    void batchSaveBySchemeId(Long schemeId, Set<Long> channelIds, LocalDateTime nowTime);

    @Select(" <script> " +
            " SELECT * FROM " + ALARM_SCHEME_CHANNEL_TABLE_NAME +
            " WHERE channel_id IN " +
            " <foreach collection='channelIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    List<AlarmSchemeChannelRel> selectByChannelIds(Set<Long> channelIds);

    @Insert({" <script> " +
            " INSERT INTO " + ALARM_SCHEME_CHANNEL_TABLE_NAME + " (scheme_id, channel_id, create_time) values " +
            " <foreach collection='alarmSchemeChannelRelList' item='item' separator=','>(#{item.schemeId}, #{item.channelId}, #{item.createTime})</foreach> " +
            " </script>"})
    void batchSave(List<AlarmSchemeChannelRel> alarmSchemeChannelRelList);

    @Update(" <script> " +
            " <foreach collection='alarmSchemeChannelRelList' item='item' separator=';'> " +
            " UPDATE " + ALARM_SCHEME_CHANNEL_TABLE_NAME +
            " SET update_time = #{item.updateTime}  " +
            " , scheme_id = #{item.schemeId} " +
            " WHERE id = #{item.id} "+
            " </foreach> " +
            " </script> ")
    void batchUpdate(List<AlarmSchemeChannelRel> alarmSchemeChannelRelList);

    @Update(" <script> " +
            " UPDATE " + ALARM_SCHEME_CHANNEL_TABLE_NAME +
            " SET update_time = #{nowTime}  " +
            " , deploy_state = #{deployState} " +
            " WHERE id IN " +
            " <foreach collection='channelIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    void batchUpdateDeployState(List<Long> channelIds, Integer deployState, LocalDateTime nowTime);

    @Delete(" <script> " +
            " DELETE FROM " + ALARM_SCHEME_CHANNEL_TABLE_NAME +
            " WHERE id IN " +
            " <foreach collection='idList' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    void batchDelete(List<Long> idList);

    @Select(" SELECT * FROM " + ALARM_SCHEME_CHANNEL_TABLE_NAME +
            " WHERE scheme_id = #{schemeId} ")
    List<GetAlarmChannelDeployRsp> selectBySchemeId(Long schemeId);

    @Select(" <script> " +
            " SELECT channel_id FROM " + ALARM_SCHEME_CHANNEL_TABLE_NAME +
            " WHERE scheme_id IN " +
            " <foreach collection='schemeIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    List<Long> selectChannelIdBySchemeIds(List<Long> schemeIds);

    @Delete(" <script> " +
            " DELETE FROM " + ALARM_SCHEME_CHANNEL_TABLE_NAME +
            " WHERE scheme_id IN " +
            " <foreach collection='schemeIds' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    void deleteBySchemeIds(List<Long> schemeIds);

}
