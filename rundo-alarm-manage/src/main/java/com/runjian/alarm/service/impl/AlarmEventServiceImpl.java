package com.runjian.alarm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.alarm.dao.AlarmEventMapper;
import com.runjian.alarm.entity.AlarmEventInfo;
import com.runjian.alarm.service.AlarmEventService;
import com.runjian.alarm.vo.response.GetAlarmEventNameRsp;
import com.runjian.alarm.vo.response.GetAlarmEventRsp;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/9/11 9:33
 */
@Service
@RequiredArgsConstructor
public class AlarmEventServiceImpl implements AlarmEventService {

    private final AlarmEventMapper alarmEventMapper;

    @Override
    public PageInfo<GetAlarmEventRsp> getAlarmEventByPage(int page, int num, String eventName, String eventCode) {
        PageHelper.startPage(page, num);
        List<GetAlarmEventRsp> alarmEventByPage = alarmEventMapper.selectAlarmEventByPage(eventName, eventCode);
        alarmEventByPage.sort(Comparator.comparing(GetAlarmEventRsp::getEventSort));
        return new PageInfo<>(alarmEventByPage);
    }

    @Override
    public List<GetAlarmEventNameRsp> getAlarmEvent(String eventName) {
        List<GetAlarmEventNameRsp> getAlarmEventNameRspList = alarmEventMapper.selectAllByEventName(eventName);
        getAlarmEventNameRspList.sort(Comparator.comparing(GetAlarmEventNameRsp::getEventSort));
        return getAlarmEventNameRspList;
    }

    @Override
    public List<GetAlarmEventNameRsp> getAlarmEventName() {
        List<GetAlarmEventNameRsp> getAlarmEventNameRspList = alarmEventMapper.selectEventName();
        getAlarmEventNameRspList.sort(Comparator.comparing(GetAlarmEventNameRsp::getEventSort));
        return getAlarmEventNameRspList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAlarmEvent(String eventName, String eventCode, Integer eventSort, String eventDesc) {
        List<AlarmEventInfo> alarmEventInfoOptional = alarmEventMapper.selectByEventNameOrEventCode(eventName, eventCode);
        if (!alarmEventInfoOptional.isEmpty()) {
            throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, "事件名称或者事件编码重复，请重新填写");
        }
        LocalDateTime nowTime = LocalDateTime.now();
        alarmEventMapper.save(new AlarmEventInfo(null, eventName, eventCode, eventSort, eventDesc, nowTime, nowTime));
    }

    @Override
    public void updateAlarmEvent(Long id, String eventName, Integer eventSort, String eventDesc) {
        Optional<AlarmEventInfo> alarmEventInfoOptional = alarmEventMapper.selectById(id);
        if (alarmEventInfoOptional.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "没有找到对应的事件");
        }
        AlarmEventInfo alarmEventInfo = alarmEventInfoOptional.get();
        LocalDateTime nowTime = LocalDateTime.now();
        alarmEventInfo.setEventName(eventName);
        alarmEventInfo.setUpdateTime(nowTime);
        alarmEventInfo.setEventSort(eventSort);
        alarmEventInfo.setEventDesc(eventDesc);
        alarmEventMapper.update(alarmEventInfo);
    }

    @Override
    public void deleteAlarmEvent(Long id) {
        if (alarmEventMapper.selectById(id).isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "没有找到对应的事件");
        }
        alarmEventMapper.deleteById(id);
    }
}
