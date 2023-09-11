package com.runjian.alarm.service.impl;

import com.runjian.alarm.dao.AlarmMsgInfoMapper;
import com.runjian.alarm.dao.AlarmSchemeInfoMapper;
import com.runjian.alarm.dao.relation.AlarmSchemeChannelRelMapper;
import com.runjian.alarm.dao.relation.AlarmSchemeEventRelMapper;
import com.runjian.alarm.service.AlarmMsgSouthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/9/11 17:12
 */

@Service
@RequiredArgsConstructor
public class AlarmMsgSouthServiceImpl implements AlarmMsgSouthService {

    private final AlarmMsgInfoMapper alarmMsgInfoMapper;

    private final AlarmSchemeInfoMapper alarmSchemeInfoMapper;

    private final AlarmSchemeEventRelMapper alarmSchemeEventRelMapper;

    private final AlarmSchemeChannelRelMapper alarmSchemeChannelRelMapper;

    @Override
    public void receiveAlarmMsg(Long channelId, String eventCode, Integer eventEndType, String eventDesc, LocalDateTime eventTime) {

    }
}
