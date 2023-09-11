package com.runjian.alarm.dao.relation;

import com.runjian.alarm.entity.relation.AlarmSchemeChannelRel;
import com.runjian.alarm.vo.response.GetAlarmChannelRsp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    List<Long> selectChannelIdBySchemeId(Long id);

    List<GetAlarmChannelRsp> selectSchemeNameByChannelIds(Set<Long> channelIds);

    void batchSave(Long id, Set<Long> channelIds, LocalDateTime nowTime);

    List<AlarmSchemeChannelRel> selectByChannelIds(Set<Long> channelIds);

    void batchSave(List<AlarmSchemeChannelRel> alarmSchemeChannelRelList);

    void batchUpdate(ArrayList<AlarmSchemeChannelRel> alarmSchemeChannelRelList);

    void deleteBySchemeId(Long id);
}
