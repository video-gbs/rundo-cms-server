package com.runjian.alarm.dao;

import com.runjian.alarm.dao.relation.AlarmSchemeChannelRelMapper;
import com.runjian.alarm.entity.AlarmEventInfo;
import com.runjian.alarm.entity.AlarmSchemeInfo;
import com.runjian.alarm.vo.response.GetAlarmSchemePageRsp;
import com.runjian.alarm.vo.response.GetAlarmSchemeRsp;
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
public interface AlarmSchemeInfoMapper {

    String ALARM_SCHEME_TABLE_NAME = "rundo_alarm_scheme";

    @Select(" <script> " +
            " SELECT * FROM " + ALARM_SCHEME_TABLE_NAME +
            " WHERE 1=1 " +
            " <if test=\"schemeName != null\" > AND scheme_name LIKE CONCAT('%', #{schemeName}, '%') </if>" +
            " <if test=\"disabled != null\" > AND disabled = #{disabled} </if>" +
            " <if test=\"createStartTime != null\" > AND create_time &gt;= #{createStartTime} </if>" +
            " <if test=\"createEndTime != null\" > AND create_time &lt;= #{createEndTime} </if> " +
            " ORDER BY update_time DESC " +
            " </script> ")
    List<GetAlarmSchemePageRsp> selectByPage(String schemeName, Integer disabled, LocalDateTime createStartTime, LocalDateTime createEndTime);

    @Select(" SELECT * FROM " + ALARM_SCHEME_TABLE_NAME +
            " WHERE id = #{id} ")
    Optional<GetAlarmSchemeRsp> selectRspById(Long id);

    @Select(" SELECT * FROM " + ALARM_SCHEME_TABLE_NAME +
            " WHERE scheme_name = #{schemeName} ")
    Optional<AlarmEventInfo> selectBySchemeName(String schemeName);

    @Insert(" INSERT INTO " + ALARM_SCHEME_TABLE_NAME + "(scheme_name, template_id, disabled, update_time, create_time) values " +
            " (#{schemeName}, #{templateId}, #{disabled}, #{updateTime}, #{createTime}) " )
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(AlarmSchemeInfo alarmSchemeInfo);

    @Select(" SELECT * FROM " + ALARM_SCHEME_TABLE_NAME +
            " WHERE id = #{id} FOR UPDATE")
    Optional<AlarmSchemeInfo> selectLockById(Long id);

    @Update(" UPDATE " + ALARM_SCHEME_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , disabled = #{disabled} " +
            " WHERE id = #{id} " )
    void updateDisabled(AlarmSchemeInfo alarmSchemeInfo);

    @Update(" UPDATE " + ALARM_SCHEME_TABLE_NAME +
            " SET update_time = #{updateTime}  " +
            " , scheme_name = #{schemeName}" +
            " ,  template_id = #{templateId}" +
            " WHERE id = #{id} ")
    void update(AlarmSchemeInfo alarmSchemeInfo);

    @Delete(" DELETE FROM " + ALARM_SCHEME_TABLE_NAME +
            " WHERE id = #{id} ")
    void deleteById(Long id);

    @Select(" SELECT ast.* FROM " + ALARM_SCHEME_TABLE_NAME + " ast " +
            " LEFT JOIN " + AlarmSchemeChannelRelMapper.ALARM_SCHEME_CHANNEL_TABLE_NAME + " asct ON ast.id = asct.scheme_id " +
            " WHERE asct.channel_id = #{channelId} ")
    Optional<AlarmSchemeInfo> selectByChannelId(Long channelId);

    @Select(" <script> " +
            " SELECT * FROM " + ALARM_SCHEME_TABLE_NAME +
            " WHERE id IN " +
            " <foreach collection='ids' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " FOR UPDATE "+
            " </script> ")
    List<AlarmSchemeInfo> selectLockByIds(Set<Long> ids);

    @Delete(" <script> " +
            " DELETE FROM " + ALARM_SCHEME_TABLE_NAME +
            " WHERE id IN " +
            " <foreach collection='ids' item='item' open='(' separator=',' close=')'> #{item} </foreach> " +
            " </script> ")
    void deleteByIds(List<Long> ids);
}
