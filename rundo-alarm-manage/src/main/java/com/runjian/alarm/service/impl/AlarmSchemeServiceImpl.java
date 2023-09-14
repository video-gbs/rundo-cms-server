package com.runjian.alarm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.alarm.dao.AlarmSchemeInfoMapper;
import com.runjian.alarm.dao.relation.AlarmSchemeChannelRelMapper;
import com.runjian.alarm.dao.relation.AlarmSchemeEventRelMapper;
import com.runjian.alarm.entity.AlarmEventInfo;
import com.runjian.alarm.entity.AlarmSchemeInfo;
import com.runjian.alarm.entity.relation.AlarmSchemeChannelRel;
import com.runjian.alarm.entity.relation.AlarmSchemeEventRel;
import com.runjian.alarm.feign.DeviceControlApi;
import com.runjian.alarm.service.AlarmSchemeService;
import com.runjian.alarm.vo.request.PutDefenseReq;
import com.runjian.alarm.vo.response.*;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/9/11 11:15
 */
@Service
@RequiredArgsConstructor
public class AlarmSchemeServiceImpl implements AlarmSchemeService {

    private final AlarmSchemeInfoMapper alarmSchemeInfoMapper;

    private final AlarmSchemeEventRelMapper alarmSchemeEventRelMapper;

    private final AlarmSchemeChannelRelMapper alarmSchemeChannelRelMapper;

    private final DeviceControlApi deviceControlApi;

    @Override
    public PageInfo<GetAlarmSchemePageRsp> getAlarmSchemeByPage(int page, int num, String schemeName, Integer disabled, LocalDateTime createTime) {
        PageHelper.startPage(page, num);
        Map<Long, GetAlarmSchemePageRsp> getAlarmSchemePageRspMap = alarmSchemeInfoMapper.selectByPage(schemeName, disabled, createTime).stream().collect(Collectors.toMap(GetAlarmSchemePageRsp::getId, getAlarmSchemePageRsp -> getAlarmSchemePageRsp));
        Map<Long, List<GetAlarmSchemeEventNameRsp>> getAlarmSchemeEventNameRsp = alarmSchemeEventRelMapper.selectEventNameBySchemeIds(getAlarmSchemePageRspMap.keySet()).stream().collect(Collectors.groupingBy(GetAlarmSchemeEventNameRsp::getSchemeId, Collectors.toList()));
        for (Map.Entry<Long, GetAlarmSchemePageRsp> entry : getAlarmSchemePageRspMap.entrySet()) {
            List<GetAlarmSchemeEventNameRsp> alarmSchemeEventNameRspList = getAlarmSchemeEventNameRsp.get(entry.getKey());
            entry.getValue().setEventNameList(alarmSchemeEventNameRspList.stream().map(GetAlarmSchemeEventNameRsp::getEventName).collect(Collectors.toList()));
        }
        return new PageInfo<>(new ArrayList<>(getAlarmSchemePageRspMap.values()));
    }

    @Override
    public GetAlarmSchemeRsp getAlarmScheme(Long id) {
        Optional<GetAlarmSchemeRsp> getAlarmSchemeRspOp = alarmSchemeInfoMapper.selectRspById(id);
        if (getAlarmSchemeRspOp.isEmpty()) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "告警预案不存在，请刷新重试");
        }
        List<Long> channelIdList = alarmSchemeChannelRelMapper.selectChannelIdBySchemeId(id);
        List<GetAlarmSchemeEventRsp> alarmSchemeEventRspList = alarmSchemeEventRelMapper.selectRspBySchemeId(id);
        GetAlarmSchemeRsp getAlarmSchemeRsp = getAlarmSchemeRspOp.get();
        getAlarmSchemeRsp.setChannelIdList(channelIdList);
        getAlarmSchemeRsp.setAlarmSchemeEventRelList(alarmSchemeEventRspList);
        return getAlarmSchemeRsp;
    }

    @Override
    public List<GetAlarmChannelRsp> getAlarmChannel(Set<Long> channelIds) {
        return alarmSchemeChannelRelMapper.selectSchemeNameByChannelIds(channelIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAlarmScheme(String schemeName, Long templateId, Set<Long> channelIds, List<AlarmSchemeEventRel> alarmSchemeEventRelList) {
        Optional<AlarmEventInfo> alarmEventInfoOp = alarmSchemeInfoMapper.selectBySchemeName(schemeName);
        if (alarmEventInfoOp.isPresent()) {
            throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, "该告警预案名称已存在，请重新输入");
        }
        LocalDateTime nowTime = LocalDateTime.now();
        AlarmSchemeInfo alarmSchemeInfo = new AlarmSchemeInfo();
        alarmSchemeInfo.setSchemeName(schemeName);
        alarmSchemeInfo.setTemplateId(templateId);
        alarmSchemeInfo.setCreateTime(nowTime);
        alarmSchemeInfo.setUpdateTime(nowTime);
        alarmSchemeInfo.setDisabled(CommonEnum.DISABLE.getCode());
        alarmSchemeInfoMapper.save(alarmSchemeInfo);

        alarmSchemeChannelRelMapper.batchSave(alarmSchemeInfo.getId(), channelIds, nowTime);

        alarmSchemeEventRelMapper.batchSave(alarmSchemeInfo.getId(), alarmSchemeEventRelList, nowTime);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAlarmSchemeDisabled(Long id, Integer disabled) {
        Optional<AlarmSchemeInfo> alarmSchemeInfoOp = alarmSchemeInfoMapper.selectLockById(id);
        if (alarmSchemeInfoOp.isEmpty()) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "告警预案不存在");
        }
        AlarmSchemeInfo alarmSchemeInfo = alarmSchemeInfoOp.get();
        if (Objects.equals(alarmSchemeInfo.getDisabled(), disabled)) {
            return;
        }
        alarmSchemeInfo.setDisabled(disabled);
        alarmSchemeInfo.setUpdateTime(LocalDateTime.now());
        alarmSchemeInfoMapper.updateDisabled(alarmSchemeInfo);
        List<Long> channelIds = alarmSchemeChannelRelMapper.selectChannelIdBySchemeId(id);
        if (channelIds.isEmpty()) {
            return;
        }
        if (CommonEnum.getBoolean(disabled)) {
            deviceControlApi.defense(new PutDefenseReq(channelIds, false));
        } else {
            deviceControlApi.defense(new PutDefenseReq(channelIds, true));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAlarmScheme(Long id, String schemeName, Long templateId, Set<Long> channelIds, List<AlarmSchemeEventRel> alarmSchemeEventRelList) {
        Optional<AlarmSchemeInfo> alarmSchemeInfoOp = alarmSchemeInfoMapper.selectLockById(id);
        if (alarmSchemeInfoOp.isEmpty()) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "告警预案不存在");
        }
        LocalDateTime nowTime = LocalDateTime.now();
        AlarmSchemeInfo alarmSchemeInfo = alarmSchemeInfoOp.get();
        if (!Objects.equals(alarmSchemeInfo.getSchemeName(), schemeName)) {
            if (alarmSchemeInfoMapper.selectBySchemeName(schemeName).isPresent()) {
                throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, "该告警预案名称已存在，请重新输入");
            }
            alarmSchemeInfo.setSchemeName(schemeName);
        }
        alarmSchemeInfo.setTemplateId(templateId);
        alarmSchemeInfo.setUpdateTime(nowTime);
        // 更新预案
        alarmSchemeInfoMapper.update(alarmSchemeInfo);

        // 更新通道
        Map<Long, AlarmSchemeChannelRel> alarmSchemeChannelRelMap = alarmSchemeChannelRelMapper.selectByChannelIds(channelIds).stream().collect(Collectors.toMap(AlarmSchemeChannelRel::getChannelId, alarmSchemeChannelRel -> alarmSchemeChannelRel));
        List<AlarmSchemeChannelRel> newSchemeChannelRelList = new ArrayList<>(channelIds.size());
        List<AlarmSchemeChannelRel> updateSchemeChannelRelList = new ArrayList<>(alarmSchemeChannelRelMap.size());
        for (Long channelId : channelIds) {
            AlarmSchemeChannelRel alarmSchemeChannelRel = alarmSchemeChannelRelMap.remove(channelId);
            if (Objects.isNull(alarmSchemeChannelRel)) {
                alarmSchemeChannelRel = new AlarmSchemeChannelRel();
                alarmSchemeChannelRel.setChannelId(channelId);
                alarmSchemeChannelRel.setSchemeId(id);
                alarmSchemeChannelRel.setCreateTime(nowTime);
                newSchemeChannelRelList.add(alarmSchemeChannelRel);
            } else {
                alarmSchemeChannelRel.setCreateTime(nowTime);
                updateSchemeChannelRelList.add(alarmSchemeChannelRel);
            }
        }
        alarmSchemeChannelRelMapper.batchSave(newSchemeChannelRelList);
        alarmSchemeChannelRelMapper.batchUpdate(updateSchemeChannelRelList);
        alarmSchemeChannelRelMapper.batchDelete(new ArrayList<>(alarmSchemeChannelRelMap.values()));

        // 更新事件信息
        Set<String> existEventCodes = alarmSchemeEventRelMapper.selectEventCodeBySchemeId(id);
        Map<String, AlarmSchemeEventRel> newEventMap = alarmSchemeEventRelList.stream().collect(Collectors.toMap(AlarmSchemeEventRel::getEventCode, alarmSchemeEventRel -> alarmSchemeEventRel));
        List<String> deleteEventCodeList = new ArrayList<>(existEventCodes.size());
        List<AlarmSchemeEventRel> updateEventList = new ArrayList<>(newEventMap.size());
        for (String eventCode : existEventCodes) {
            AlarmSchemeEventRel alarmSchemeEventRel = newEventMap.remove(eventCode);
            if (Objects.isNull(alarmSchemeEventRel)) {
                deleteEventCodeList.add(eventCode);
            } else {
                updateEventList.add(alarmSchemeEventRel);
            }
        }
        alarmSchemeEventRelMapper.batchDeleteBySchemeIdAndEventCodes(id, deleteEventCodeList);
        alarmSchemeEventRelMapper.batchUpdate(updateEventList);
        alarmSchemeEventRelMapper.batchSave(new ArrayList<>(newEventMap.values()));

        if (channelIds.isEmpty()) {
            return;
        }
        if (CommonEnum.getBoolean(alarmSchemeInfo.getDisabled())) {
            channelIds.addAll(alarmSchemeChannelRelMap.values().stream().map(AlarmSchemeChannelRel::getChannelId).collect(Collectors.toList()));
            deviceControlApi.defense(new PutDefenseReq(new ArrayList<>(channelIds), false));
        } else {
            // 撤防删除的设备
            deviceControlApi.defense(new PutDefenseReq(alarmSchemeChannelRelMap.values().stream().map(AlarmSchemeChannelRel::getChannelId).collect(Collectors.toList()), false));
            // 布防新增的设备
            deviceControlApi.defense(new PutDefenseReq(new ArrayList<>(channelIds), true));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlarmScheme(Long id) {
        Optional<AlarmSchemeInfo> alarmSchemeInfoOp = alarmSchemeInfoMapper.selectLockById(id);
        if (alarmSchemeInfoOp.isEmpty()) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "告警预案不存在");
        }
        AlarmSchemeInfo alarmSchemeInfo = alarmSchemeInfoOp.get();
        if (!CommonEnum.getBoolean(alarmSchemeInfo.getDisabled())) {
            List<Long> channelIds = alarmSchemeChannelRelMapper.selectChannelIdBySchemeId(id);
            if (channelIds.isEmpty()) {
                return;
            }
            deviceControlApi.defense(new PutDefenseReq(channelIds, true));
            alarmSchemeChannelRelMapper.deleteBySchemeId(id);
        }
        alarmSchemeEventRelMapper.deleteBySchemeId(id);
        alarmSchemeInfoMapper.deleteById(id);
    }

}
