package com.runjian.alarm.dao.relation;

import com.runjian.alarm.entity.relation.AlarmSchemeEventRel;
import com.runjian.alarm.vo.response.GetAlarmSchemeEventNameRsp;
import com.runjian.alarm.vo.response.GetAlarmSchemeEventRsp;
import org.apache.ibatis.annotations.Mapper;
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

    List<GetAlarmSchemeEventNameRsp> selectEventNameBySchemeIds(Set<Long> schemeIds);

    List<GetAlarmSchemeEventRsp> selectRspBySchemeId(Long id);

    void batchSave(Long id, List<AlarmSchemeEventRel> alarmSchemeEventRelList, LocalDateTime nowTime);

    void batchSave(List<AlarmSchemeEventRel> alarmSchemeEventRelList);

    void batchUpdate(List<AlarmSchemeEventRel> alarmSchemeEventRelList);

    Set<String> selectEventCodeBySchemeId(Long id);

    void batchDeleteBySchemeIdAndEventCodes(Long id, List<String> deleteEventCodeList);

    void deleteBySchemeId(Long id);

    Optional<AlarmSchemeEventRel> selectBySchemeIdAndEventCode(Long id, String eventCode);
}
