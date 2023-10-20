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
import com.runjian.alarm.feign.TimerUtilsApi;
import com.runjian.alarm.service.AlarmSchemeService;
import com.runjian.alarm.vo.request.PutDefenseReq;
import com.runjian.alarm.vo.response.*;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.LogTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/9/11 11:15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmSchemeServiceImpl implements AlarmSchemeService {

    private final AlarmSchemeInfoMapper alarmSchemeInfoMapper;

    private final AlarmSchemeEventRelMapper alarmSchemeEventRelMapper;

    private final AlarmSchemeChannelRelMapper alarmSchemeChannelRelMapper;

    private final DeviceControlApi deviceControlApi;

    private final TimerUtilsApi timerUtilsApi;

    @Override
    public PageInfo<GetAlarmSchemePageRsp> getAlarmSchemeByPage(int page, int num, String schemeName, Integer disabled, LocalDateTime createStartTime, LocalDateTime createEndTime) {
        PageHelper.startPage(page, num);
        Map<Long, GetAlarmSchemePageRsp> getAlarmSchemePageRspMap = alarmSchemeInfoMapper.selectByPage(schemeName, disabled, createStartTime, createEndTime).stream().collect(Collectors.toMap(GetAlarmSchemePageRsp::getId, getAlarmSchemePageRsp -> getAlarmSchemePageRsp));
        if (getAlarmSchemePageRspMap.isEmpty()){
            return new PageInfo<>();
        }
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

        alarmSchemeChannelRelMapper.batchSaveBySchemeId(alarmSchemeInfo.getId(), channelIds, nowTime);

        alarmSchemeEventRelMapper.batchSaveBySchemeId(alarmSchemeInfo.getId(), alarmSchemeEventRelList, nowTime);

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
            defense(channelIds, false);
        } else {
            defense(channelIds, true);
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
        if (!newSchemeChannelRelList.isEmpty()){
            alarmSchemeChannelRelMapper.batchSave(newSchemeChannelRelList);
        }
        if (!updateSchemeChannelRelList.isEmpty()){
            alarmSchemeChannelRelMapper.batchUpdate(updateSchemeChannelRelList);
        }
        if (!alarmSchemeChannelRelMap.values().isEmpty()){
            alarmSchemeChannelRelMapper.batchDelete(alarmSchemeChannelRelMap.values().stream().map(AlarmSchemeChannelRel::getId).collect(Collectors.toList()));
        }

        // 更新事件信息
        Set<String> existEventCodes = alarmSchemeEventRelMapper.selectEventCodeBySchemeId(id);
        log.warn("alarmSchemeEventRelList:{}", alarmSchemeEventRelList);
        Map<String, AlarmSchemeEventRel> newEventMap = alarmSchemeEventRelList.stream().collect(Collectors.toMap(AlarmSchemeEventRel::getEventCode, alarmSchemeEventRel -> alarmSchemeEventRel));
        log.warn("1newEventMap:{}", newEventMap);
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
        if (!deleteEventCodeList.isEmpty()){
            alarmSchemeEventRelMapper.batchDeleteBySchemeIdAndEventCodes(id, deleteEventCodeList);
        }
        if (!updateEventList.isEmpty()){
            alarmSchemeEventRelMapper.batchUpdate(updateEventList);
        }
        if (!newEventMap.values().isEmpty()){
            log.warn("2newEventMap:{}", newEventMap);
            alarmSchemeEventRelMapper.batchSave(new ArrayList<>(newEventMap.values()));
        }

        if (channelIds.isEmpty()) {
            return;
        }
        if (CommonEnum.getBoolean(alarmSchemeInfo.getDisabled())) {
            channelIds.addAll(alarmSchemeChannelRelMap.values().stream().map(AlarmSchemeChannelRel::getChannelId).collect(Collectors.toList()));
            defense(new ArrayList<>(channelIds), false);
        } else {
            // 撤防删除的设备
            defense(alarmSchemeChannelRelMap.values().stream().map(AlarmSchemeChannelRel::getChannelId).collect(Collectors.toList()), false);
            // 布防新增的设备
            defense(new ArrayList<>(channelIds), true);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlarmScheme(Set<Long> ids) {
        List<AlarmSchemeInfo> alarmSchemeInfoList = alarmSchemeInfoMapper.selectLockByIds(ids);

        List<Long> deploySchemeIds = new ArrayList<>(alarmSchemeInfoList.size());
        for (AlarmSchemeInfo alarmSchemeInfo : alarmSchemeInfoList){
            if (!CommonEnum.getBoolean(alarmSchemeInfo.getDisabled())) {
                deploySchemeIds.add(alarmSchemeInfo.getId());
            }
        }
        if(!deploySchemeIds.isEmpty()){
            List<Long> channelIds = alarmSchemeChannelRelMapper.selectChannelIdBySchemeIds(deploySchemeIds);
            if (!channelIds.isEmpty()) {
                defense(channelIds, true);
                alarmSchemeChannelRelMapper.deleteBySchemeIds(deploySchemeIds);
            }
        }
        List<Long> schemeIds = alarmSchemeInfoList.stream().map(AlarmSchemeInfo::getId).collect(Collectors.toList());
        alarmSchemeEventRelMapper.deleteBySchemeIds(schemeIds);
        alarmSchemeInfoMapper.deleteByIds(schemeIds);
    }

    @Override
    public PageInfo<GetAlarmChannelDeployRsp> getAlarmChannelDeploy(int page, int num, Long schemeId) {
        PageHelper.startPage(page, num);
        return new PageInfo<>(alarmSchemeChannelRelMapper.selectBySchemeId(schemeId));
    }

    @Override
    public void defense(List<Long> channelIdList, boolean isDeploy){
        CommonResponse<Set<Long>> commonResponse = deviceControlApi.defense(new PutDefenseReq(channelIdList, isDeploy));
        if (commonResponse.isError()){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "告警预案服务", isDeploy ? "发送布防失败" : "发送撤防失败", String.format("%s:%s", commonResponse.getMsg(), commonResponse.getData()), channelIdList);
            return;
        }
        Set<Long> channelIds = commonResponse.getData();
        if (Objects.isNull(channelIds) || channelIds.isEmpty()){
            return;
        }
        channelIdList.removeAll(channelIds);
        alarmSchemeChannelRelMapper.batchUpdateDeployState(channelIdList, isDeploy ? 1 : 0, LocalDateTime.now());
    }
}
