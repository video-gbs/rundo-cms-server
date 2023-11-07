package com.runjian.alarm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.alarm.dao.AlarmMsgInfoMapper;
import com.runjian.alarm.service.AlarmMsgNorthService;
import com.runjian.alarm.vo.response.GetAlarmMsgRsp;
import com.runjian.common.aspect.annotation.BlankStringValid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/9/11 17:07
 */
@Service
@RequiredArgsConstructor
public class AlarmMsgNorthServiceImpl implements AlarmMsgNorthService {

    private final AlarmMsgInfoMapper alarmMsgInfoMapper;

    @Override
    @BlankStringValid
    public PageInfo<GetAlarmMsgRsp> getAlarmMsgByPage(int page, int num, Long channelId, String alarmDesc, LocalDateTime alarmStartTime, LocalDateTime alarmEndTime) {
        PageHelper.startPage(page, num);
        return new PageInfo<>(alarmMsgInfoMapper.selectByAlarmDescAndAlarmTime(channelId, alarmDesc, alarmStartTime, alarmEndTime));
    }

    @Override
    public void deleteAlarmMsg(List<Long> idList) {
        if (idList.isEmpty()){
            return;
        }
        alarmMsgInfoMapper.deleteByIdList(idList);
    }
}
