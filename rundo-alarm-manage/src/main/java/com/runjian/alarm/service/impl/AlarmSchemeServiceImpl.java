package com.runjian.alarm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.alarm.dao.AlarmEventMapper;
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
import com.runjian.alarm.vo.feign.PostUseTemplateReq;
import com.runjian.alarm.vo.feign.PutUnUseTemplateReq;
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
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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

    private final AlarmEventMapper alarmEventMapper;

    private final DeviceControlApi deviceControlApi;

    private final TimerUtilsApi timerUtilsApi;

    private final ThreadPoolExecutor defenseThreadPool = new ThreadPoolExecutor(
            1,
            5,
            10,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(30), r -> {
        Thread t = new Thread(r);
        t.setName("defense-thread");
        // 判断是否是守护线程，如果是关闭
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        // 判断是否是正常优先级，如果不是调整会正常优先级
        if (Thread.NORM_PRIORITY != t.getPriority()) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    },
            new ThreadPoolExecutor.DiscardPolicy());

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
        ArrayList<GetAlarmSchemePageRsp> getAlarmSchemePageRspList = new ArrayList<>(getAlarmSchemePageRspMap.values());
        getAlarmSchemePageRspList.sort(Comparator.comparing(GetAlarmSchemePageRsp::getUpdateTime).reversed());
        return new PageInfo<>(getAlarmSchemePageRspList);
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
        Set<String> eventCodes = alarmSchemeEventRelList.stream().map(AlarmSchemeEventRel::getEventCode).collect(Collectors.toSet());
        List<AlarmEventInfo> alarmEventInfoList = alarmEventMapper.selectByEventCodes(eventCodes);
        if (!Objects.equals(alarmEventInfoList.size(), eventCodes.size())){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "存在非法事件编码");
        }
        LocalDateTime nowTime = LocalDateTime.now();
        AlarmSchemeInfo alarmSchemeInfo = new AlarmSchemeInfo();
        alarmSchemeInfo.setSchemeName(schemeName);
        alarmSchemeInfo.setTemplateId(templateId);
        alarmSchemeInfo.setCreateTime(nowTime);
        alarmSchemeInfo.setUpdateTime(nowTime);
        alarmSchemeInfo.setDisabled(CommonEnum.DISABLE.getCode());
        alarmSchemeInfoMapper.save(alarmSchemeInfo);

        if (!channelIds.isEmpty()){
            List<AlarmSchemeChannelRel> alarmSchemeChannelRelList = alarmSchemeChannelRelMapper.selectByChannelIds(channelIds);
            if (!CollectionUtils.isEmpty(alarmSchemeChannelRelList)) {
                List<Long> existChannelIds = alarmSchemeChannelRelList.stream().map(AlarmSchemeChannelRel::getChannelId).collect(Collectors.toList());
                alarmSchemeChannelRelMapper.batchUpdateSchemeId(alarmSchemeInfo.getId(), existChannelIds, nowTime);
                existChannelIds.forEach(channelIds::remove);
            }
            if (!channelIds.isEmpty()){
                alarmSchemeChannelRelMapper.batchSaveBySchemeId(alarmSchemeInfo.getId(), channelIds, nowTime);
            }
        }

        alarmSchemeEventRelMapper.batchSaveBySchemeId(alarmSchemeInfo.getId(), alarmSchemeEventRelList, nowTime);

        timerUtilsApi.useTemplate(new PostUseTemplateReq(templateId, "alarm-manage", String.valueOf(alarmSchemeInfo.getId()), 0));

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
        Map<String, AlarmSchemeEventRel> newEventMap = alarmSchemeEventRelList.stream().collect(Collectors.toMap(AlarmSchemeEventRel::getEventCode, alarmSchemeEventRel -> alarmSchemeEventRel));
        List<AlarmEventInfo> alarmEventInfoList = alarmEventMapper.selectByEventCodes(newEventMap.keySet());
        if (!Objects.equals(alarmEventInfoList.size(), newEventMap.keySet().size())){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "存在非法事件编码");
        }
        if (!Objects.equals(alarmSchemeInfo.getTemplateId(), templateId)){
            alarmSchemeInfo.setTemplateId(templateId);
            timerUtilsApi.useTemplate(new PostUseTemplateReq(templateId, "alarm-manage", String.valueOf(alarmSchemeInfo.getId()), 0));
        }

        alarmSchemeInfo.setUpdateTime(nowTime);
        // 更新预案
        alarmSchemeInfoMapper.update(alarmSchemeInfo);



        // 更新事件信息

        Set<String> existEventCodes = alarmSchemeEventRelMapper.selectEventCodeBySchemeId(id);

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
            alarmSchemeEventRelMapper.batchSave(new ArrayList<>(newEventMap.values()));
        }

        // 更新通道
        if (!channelIds.isEmpty()){
            Map<Long, AlarmSchemeChannelRel> alarmSchemeChannelRelMap = alarmSchemeChannelRelMapper.selectBySchemeIdOrChannelIds(alarmSchemeInfo.getId(), channelIds).stream().collect(Collectors.toMap(AlarmSchemeChannelRel::getChannelId, alarmSchemeChannelRel -> alarmSchemeChannelRel));
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
            if (CommonEnum.getBoolean(alarmSchemeInfo.getDisabled())) {
                if (!alarmSchemeChannelRelMap.values().isEmpty()){
                    channelIds.addAll(alarmSchemeChannelRelMap.values().stream().map(AlarmSchemeChannelRel::getChannelId).collect(Collectors.toList()));
                }
                defense(new ArrayList<>(channelIds), false);
            } else {
                // 撤防删除的设备
                if (!alarmSchemeChannelRelMap.values().isEmpty()){
                    defense(alarmSchemeChannelRelMap.values().stream().map(AlarmSchemeChannelRel::getChannelId).collect(Collectors.toList()), false);
                }
                // 布防新增的设备
                defense(new ArrayList<>(channelIds), true);
            }
        } else {
            List<GetAlarmChannelDeployRsp> getAlarmChannelDeployRsps = alarmSchemeChannelRelMapper.selectBySchemeId(alarmSchemeInfo.getId());
            if (CollectionUtils.isEmpty(getAlarmChannelDeployRsps)){
                return;
            }
            defense(new ArrayList<>(getAlarmChannelDeployRsps.stream().map(GetAlarmChannelDeployRsp::getChannelId).collect(Collectors.toList())), false);
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
            timerUtilsApi.unUseTemplate(new PutUnUseTemplateReq("alarm-manage", String.valueOf(alarmSchemeInfo.getId())));
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
        defenseThreadPool.execute( () -> {
            log.warn(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "告警预案服务", isDeploy ? "发送布防" : "发送撤防", channelIdList);
            CommonResponse<Set<Long>> commonResponse = deviceControlApi.defense(new PutDefenseReq(channelIdList, isDeploy));
            if (commonResponse.isError()){
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "告警预案服务", isDeploy ? "发送布防失败" : "发送撤防失败", String.format("%s:%s", commonResponse.getMsg(), commonResponse.getData()), channelIdList);
                return;
            }
            log.error(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "告警预案服务", isDeploy ? "发送布防返回" : "发送撤防返回", String.format("%s:%s", commonResponse.getMsg(), commonResponse.getData()));
            Set<Long> channelIds = commonResponse.getData();
            if (!CollectionUtils.isEmpty(channelIds)){
                channelIdList.removeAll(channelIds);
            }
            if (!channelIdList.isEmpty()){
                alarmSchemeChannelRelMapper.batchUpdateDeployState(channelIdList, isDeploy ? 1 : 0, LocalDateTime.now());
            }
        });
    }
}
