package com.runjian.alarm.dao;

import com.runjian.alarm.entity.AlarmEventInfo;
import com.runjian.alarm.entity.AlarmSchemeInfo;
import com.runjian.alarm.vo.response.GetAlarmSchemePageRsp;
import com.runjian.alarm.vo.response.GetAlarmSchemeRsp;
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
public interface AlarmSchemeInfoMapper {

    String ALARM_SCHEME_TABLE_NAME = "rundo_alarm_scheme";

    List<GetAlarmSchemePageRsp> selectByPage(String schemeName, Integer disabled, LocalDateTime createTime);

    Optional<GetAlarmSchemeRsp> selectRspById(Long id);

    Optional<AlarmEventInfo> selectBySchemeName(String schemeName);

    void save(AlarmSchemeInfo alarmSchemeInfo);

    Optional<AlarmSchemeInfo> selectLockById(Long id);

    void updateDisabled(AlarmSchemeInfo alarmSchemeInfo);

    void update(AlarmSchemeInfo alarmSchemeInfo);

    void deleteById(Long id);
}
