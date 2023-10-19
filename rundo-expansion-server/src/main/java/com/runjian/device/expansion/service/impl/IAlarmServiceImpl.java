package com.runjian.device.expansion.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.CommonEnum;
import com.runjian.device.expansion.entity.ChannelPresetLists;
import com.runjian.device.expansion.entity.DeviceChannelExpansion;
import com.runjian.device.expansion.feign.AlarmManageApi;
import com.runjian.device.expansion.feign.AuthRbacServerApi;
import com.runjian.device.expansion.mapper.ChannelPresetMapper;
import com.runjian.device.expansion.mapper.DeviceChannelExpansionMapper;
import com.runjian.device.expansion.service.IAlarmService;
import com.runjian.device.expansion.vo.feign.request.GetCatalogueResourceRsp;
import com.runjian.device.expansion.vo.feign.response.GetAlarmChannelRsp;
import com.runjian.device.expansion.vo.feign.response.PageListResp;
import com.runjian.device.expansion.vo.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/10/17 16:22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IAlarmServiceImpl implements IAlarmService {

    private final AlarmManageApi alarmManageApi;

    private final DeviceChannelExpansionMapper deviceChannelExpansionMapper;

    private final AuthRbacServerApi authRbacServerApi;

    @Override
    public PageResp<GetAlarmSchemeChannelRsp> getAlarmSchemeChannel(int page, int num, Long videoAreaId, Integer includeEquipment, String channelName, String deviceName, Integer onlineState) {
        CommonResponse<List<GetCatalogueResourceRsp>> catalogueResourceRsp = authRbacServerApi.getCatalogueResourceRsp(videoAreaId, CommonEnum.getBoolean(includeEquipment));
        catalogueResourceRsp.ifErrorThrowException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
        List<GetCatalogueResourceRsp> channelList = catalogueResourceRsp.getData();
        if(ObjectUtils.isEmpty(channelList)){
            return new PageResp<>();
        }
        List<Long> channelIds = channelList.stream().map(getCatalogueResourceRsp -> Long.parseLong(getCatalogueResourceRsp.getResourceValue())).collect(Collectors.toList());

        Page<GetAlarmSchemeChannelRsp> channelExpansionPage = deviceChannelExpansionMapper.listAlarmPage(new Page<>(page, num),channelIds, channelName, deviceName, onlineState);
        if(ObjectUtils.isEmpty(channelExpansionPage.getRecords())){
            return new PageResp<>();
        }
        CommonResponse<List<GetAlarmChannelRsp>> response = alarmManageApi.getAlarmChannel(new HashSet<>(channelIds));
        catalogueResourceRsp.ifErrorThrowException(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
        if (!ObjectUtils.isEmpty(response.getData())){
            Map<Long, GetAlarmChannelRsp> getAlarmSchemeChannelRspMap = response.getData().stream().collect(Collectors.toMap(GetAlarmChannelRsp::getChannelId, getAlarmChannelRsp -> getAlarmChannelRsp));
            for (GetAlarmSchemeChannelRsp getAlarmSchemeChannelRsp : channelExpansionPage.getRecords()){
                GetAlarmChannelRsp getAlarmChannelRsp = getAlarmSchemeChannelRspMap.get(getAlarmSchemeChannelRsp.getChannelId());
                if (Objects.isNull(getAlarmChannelRsp)){
                    continue;
                }
                getAlarmSchemeChannelRsp.setSchemeId(getAlarmChannelRsp.getSchemeId());
                getAlarmSchemeChannelRsp.setSchemeName(getAlarmChannelRsp.getSchemeName());
            }
        }

        PageResp<GetAlarmSchemeChannelRsp> getAlarmSchemeChannelRspPageResp = new PageResp<>();
        BeanUtils.copyProperties(channelExpansionPage, getAlarmSchemeChannelRspPageResp);
        return getAlarmSchemeChannelRspPageResp;
    }

    @Override
    public PageListResp<GetAlarmDeployChannelRsp> getAlarmDeployChannel(int page, int num, Long schemeId) {
        CommonResponse<PageListResp<GetAlarmDeployChannelRsp>> response = alarmManageApi.getChannelDeploy(page, num, schemeId);
        response.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        if (Objects.isNull(response.getData())){
            return new PageListResp<>();
        }
        PageListResp<GetAlarmDeployChannelRsp> deployChannelRsp = response.getData();
        List<GetAlarmDeployChannelRsp> dataList = deployChannelRsp.getList();
        if (Objects.isNull(dataList) || dataList.isEmpty()){
            return new PageListResp<>();
        }
        Map<Long, GetAlarmDeployChannelRsp> dataMap = dataList.stream().collect(Collectors.toMap(GetAlarmDeployChannelRsp::getChannelId, getAlarmDeployChannelRsp -> getAlarmDeployChannelRsp));
        List<DeviceChannelExpansion> deviceChannelExpansionList = deviceChannelExpansionMapper.selectBatchIds(dataMap.keySet());
        for (DeviceChannelExpansion deviceChannelExpansion : deviceChannelExpansionList){
            dataMap.get(deviceChannelExpansion.getId()).setChannelName(deviceChannelExpansion.getChannelName());
        }
        deployChannelRsp.setList(new ArrayList<>(dataMap.values()));
        return deployChannelRsp;
    }

    @Override
    public PageListResp<GetAlarmMsgChannelRsp> getAlarmMsgChannel(int page, int num, String eventCode, LocalDateTime startTime, LocalDateTime endTime) {
        CommonResponse<PageListResp<GetAlarmMsgChannelRsp>> response = alarmManageApi.getAlarmMsgPage(page, num, eventCode, startTime, endTime);
        response.ifErrorThrowException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR);
        if (Objects.isNull(response.getData())){
            return new PageListResp<>();
        }
        PageListResp<GetAlarmMsgChannelRsp> dataPage = response.getData();
        List<GetAlarmMsgChannelRsp> dataList = dataPage.getList();
        if (Objects.isNull(dataList) || dataList.isEmpty()){
            return new PageListResp<>();
        }
        Map<Long, GetAlarmMsgChannelRsp> dataMap = dataList.stream().collect(Collectors.toMap(GetAlarmMsgChannelRsp::getChannelId, getAlarmMsgChannelRsp -> getAlarmMsgChannelRsp));
        List<GetAlarmDeviceChannelRsp> deviceChannelRspList = deviceChannelExpansionMapper.listAlarmList(dataMap.keySet());
        for (GetAlarmDeviceChannelRsp getAlarmDeviceChannelRsp : deviceChannelRspList){
            GetAlarmMsgChannelRsp getAlarmMsgChannelRsp = dataMap.get(getAlarmDeviceChannelRsp.getId());
            getAlarmMsgChannelRsp.setChannelName(getAlarmDeviceChannelRsp.getChannelName());
            getAlarmMsgChannelRsp.setDeviceName(getAlarmDeviceChannelRsp.getDeviceName());
        }
        dataPage.setList(new ArrayList<>(dataMap.values()));
        return dataPage;
    }
}
