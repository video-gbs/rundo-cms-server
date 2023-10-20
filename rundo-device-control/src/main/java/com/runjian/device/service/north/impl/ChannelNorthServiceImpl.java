package com.runjian.device.service.north.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.*;
import com.runjian.common.utils.DateUtils;
import com.runjian.device.constant.DetailType;
import com.runjian.device.constant.SignState;
import com.runjian.device.constant.SubMsgType;
import com.runjian.device.dao.ChannelMapper;
import com.runjian.device.dao.DetailMapper;
import com.runjian.device.dao.DeviceMapper;
import com.runjian.device.entity.ChannelInfo;
import com.runjian.device.entity.DetailInfo;
import com.runjian.device.entity.DeviceInfo;
import com.runjian.device.entity.GatewayInfo;
import com.runjian.device.feign.ParsingEngineApi;
import com.runjian.device.service.common.DataBaseService;
import com.runjian.device.service.common.MessageBaseService;
import com.runjian.device.service.north.ChannelNorthService;
import com.runjian.device.vo.feign.DeviceControlReq;
import com.runjian.device.vo.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 通道北向服务
 *
 * @author Miracle
 * @date 2023/1/9 10:20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelNorthServiceImpl implements ChannelNorthService {

    private final ChannelMapper channelMapper;

    private final DeviceMapper deviceMapper;

    private final ParsingEngineApi parsingEngineApi;

    private final DetailMapper detailMapper;

    private final DataBaseService dataBaseService;

    private final MessageBaseService messageBaseService;


    @Override
    public PageInfo<GetChannelByPageRsp> getChannelByPage(int page, int num, String name) {
        List<Long> deviceIds = deviceMapper.selectIdBySignState(SignState.SUCCESS.getCode());
        if (deviceIds.isEmpty()){
            return new PageInfo<>();
        }
        PageHelper.startPage(page, num);
        return new PageInfo<>(channelMapper.selectByPage(deviceIds, name));
    }

    /**
     * 通道同步
     * @param deviceId 设备ID
     * @return 通道同步信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChannelSyncRsp channelSync(Long deviceId) {
        DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(deviceId);
        if (deviceInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())) {
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, String.format("设备%s处于离线状态", deviceId));
        }
        if (!deviceInfo.getSignState().equals(SignState.SUCCESS.getCode())) {
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, String.format("设备%s是未注册成功的设备，不允许操作", deviceId));
        }
        CommonResponse<?> response = parsingEngineApi.customEvent(new DeviceControlReq(deviceId, IdType.DEVICE, MsgType.CHANNEL_SYNC, 15000L));
        if (response.isError()) {
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "通道北向服务", "通道同步失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        ChannelSyncRsp channelSyncRsp = JSONObject.parseObject(JSONObject.toJSONString(response.getData()), ChannelSyncRsp.class);
        List<ChannelDetailRsp> channelDetailList = channelSyncRsp.getChannelDetailList();
        channelSyncRsp.setNum(channelDetailList.size());
        Set<Long> channelIds = channelDetailList.stream().map(ChannelDetailRsp::getChannelId).collect(Collectors.toSet());
        boolean checkRepeat = !Objects.equals(channelDetailList.size(), channelIds.size());
        // 判断是否有通道
        if (channelSyncRsp.getNum() > 0) {
            List<ChannelInfo> channelSaveList = new ArrayList<>(channelSyncRsp.getNum());
            List<ChannelInfo> channelUpdateList = new ArrayList<>(channelSyncRsp.getNum());
            List<DetailInfo> detailSaveList = new ArrayList<>(channelSyncRsp.getNum());
            List<DetailInfo> detailUpdateList = new ArrayList<>(channelSyncRsp.getNum());
            Map<Long, Object> channelOnlineMap = new HashMap<>(channelSyncRsp.getNum());

            LocalDateTime nowTime = LocalDateTime.now();
            // 循环通道进行添加
            for (ChannelDetailRsp rsp : channelDetailList) {
                if (checkRepeat && !channelIds.remove(rsp.getChannelId())){
                    continue;
                }

                Optional<ChannelInfo> channelInfoOp = channelMapper.selectById(rsp.getChannelId());
                ChannelInfo channelInfo = channelInfoOp.orElse(new ChannelInfo());
                // 判断通道信息是否已存在
                if (channelInfoOp.isEmpty()) {
                    channelInfo.setId(rsp.getChannelId());
                    channelInfo.setChannelType(rsp.getChannelType());
                    channelInfo.setSignState(SignState.TO_BE_ADD.getCode());
                    channelInfo.setDeviceId(deviceId);
                    channelInfo.setCreateTime(nowTime);
                    channelInfo.setUpdateTime(nowTime);
                    channelInfo.setOnlineState(CommonEnum.DISABLE.getCode());
                    channelSaveList.add(channelInfo);
                } else {
                    channelInfo.setUpdateTime(nowTime);
                    channelUpdateList.add(channelInfo);
                }
                // 更新通道在线状态
                if (channelInfo.getSignState().equals(SignState.SUCCESS.getCode()) && !Objects.equals(rsp.getOnlineState(), channelInfo.getOnlineState())) {
                    // 对通道在线状态缓存
                    channelOnlineMap.put(channelInfo.getId(), rsp.getOnlineState());
                }
                channelInfo.setOnlineState(rsp.getOnlineState());

                Optional<DetailInfo> detailInfoOp = detailMapper.selectByDcIdAndType(rsp.getChannelId(), DetailType.CHANNEL.getCode());
                DetailInfo detailInfo = detailInfoOp.orElse(new DetailInfo());
                detailInfo.setDcId(rsp.getChannelId());
                detailInfo.setOriginId(rsp.getOriginId());
                detailInfo.setIp(rsp.getIp());
                detailInfo.setPort(rsp.getPort());
                detailInfo.setName(rsp.getName());
                detailInfo.setManufacturer(rsp.getManufacturer());
                detailInfo.setModel(rsp.getModel());
                detailInfo.setFirmware(rsp.getFirmware());
                detailInfo.setPtzType(rsp.getPtzType());
                detailInfo.setUpdateTime(nowTime);
                // 判断数据是否为空，根据情况保存或者更新
                if (detailInfoOp.isEmpty()) {
                    detailInfo.setType(DetailType.CHANNEL.getCode());
                    detailInfo.setCreateTime(nowTime);
                    detailSaveList.add(detailInfo);
                } else {
                    detailUpdateList.add(detailInfo);
                }
            }
            // 更新通道的在线状态
            messageBaseService.msgDistribute(SubMsgType.CHANNEL_ONLINE_STATE, channelOnlineMap);
            // 对数据进行批量保存
            if (!channelSaveList.isEmpty())
                channelMapper.batchSave(channelSaveList);
            if (!channelUpdateList.isEmpty())
                channelMapper.batchUpdateOnlineState(channelUpdateList);
            if (!detailSaveList.isEmpty())
                detailMapper.batchSave(detailSaveList);
            if (!detailUpdateList.isEmpty())
                detailMapper.batchUpdate(detailUpdateList);
        }
        return channelSyncRsp;
    }

    /**
     * 通道注册状态转为成功
     * @param channelIdList 通道Id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void channelSignSuccess(List<Long> channelIdList) {
        List<ChannelInfo> channelInfoList = channelMapper.selectByIds(channelIdList);
        if (channelIdList.size() > channelInfoList.size()){
            List<Long> collect = channelInfoList.stream().map(ChannelInfo::getId).collect(Collectors.toList());
            channelIdList.removeAll(collect);
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, String.format("缺失的数据%s", channelIdList));
        }

        Set<Long> hasErrorChannelIds = new HashSet<>(channelIdList.size());
        for (ChannelInfo channelInfo : channelInfoList){
            if (channelInfo.getSignState().equals(SignState.DELETED.getCode())){
                try{
                    CommonResponse<?> response = parsingEngineApi.customEvent(new DeviceControlReq(channelInfo.getId(), IdType.CHANNEL, MsgType.CHANNEL_DELETE_RECOVER, 15000L));
                    if (response.isError()){
                        log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "通道北向服务", "通道恢复失败", response.getData(), response.getMsg());
                        throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
                    }
                }catch (Exception ex){
                    log.error(LogTemplate.ERROR_LOG_TEMPLATE, "通道北向服务", "通道恢复失败", ex);
                    hasErrorChannelIds.add(channelInfo.getId());
                    continue;
                }
            }
            channelInfo.setSignState(SignState.SUCCESS.getCode());
            channelInfo.setUpdateTime(LocalDateTime.now());
        }

        List<ChannelInfo> newChannelInfoList = channelInfoList.stream().filter(channelInfo -> !hasErrorChannelIds.contains(channelInfo.getId())).collect(Collectors.toList());
        channelMapper.batchUpdateSignState(newChannelInfoList);
        messageBaseService.msgDistribute(SubMsgType.CHANNEL_ADD_OR_DELETE_STATE, newChannelInfoList.stream().collect(Collectors.toMap(ChannelInfo::getId, JSONObject::toJSONString)));
        messageBaseService.msgDistribute(SubMsgType.CHANNEL_ONLINE_STATE,newChannelInfoList.stream().collect(Collectors.toMap(ChannelInfo::getId, ChannelInfo::getOnlineState)));
        if (!hasErrorChannelIds.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, String.format("通道恢复失败，失败通道id为：%s", hasErrorChannelIds));
        }
    }

    /**
     * 通道删除
     * @param channelId 通道id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void channelDeleteSoft(Long channelId) {
        ChannelInfo channelInfo = dataBaseService.getChannelInfo(channelId);
        if (channelInfo.getSignState().equals(SignState.DELETED.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "通道已进行软删除，请勿重复操作");
        }
        DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(channelInfo.getDeviceId());
        GatewayInfo gatewayInfo = dataBaseService.getGatewayInfo(deviceInfo.getGatewayId());
        if (gatewayInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "网关离线，无法操作");
        }
        channelInfo.setSignState(SignState.DELETED.getCode());
        channelInfo.setUpdateTime(LocalDateTime.now());
        channelMapper.updateSignState(channelInfo);
        CommonResponse<?> commonResponse = parsingEngineApi.customEvent(new DeviceControlReq(channelId, IdType.CHANNEL, MsgType.CHANNEL_DELETE_SOFT, 1500L));
        if (commonResponse.isError()){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "通道北向服务", "通道软删除失败", commonResponse.getData(), commonResponse.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, commonResponse.getMsg());
        }
        messageBaseService.msgDistribute(SubMsgType.CHANNEL_ADD_OR_DELETE_STATE, Map.of(channelId, JSONObject.toJSONString(channelInfo)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void channelDeleteHard(Long channelId) {
        ChannelInfo channelInfo = dataBaseService.getChannelInfo(channelId);
        if (!channelInfo.getSignState().equals(SignState.DELETED.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "通道未进行软删除，无法进行强制删除");
        }
        DeviceInfo deviceInfo = dataBaseService.getDeviceInfo(channelInfo.getDeviceId());
        GatewayInfo gatewayInfo = dataBaseService.getGatewayInfo(deviceInfo.getGatewayId());
        if (gatewayInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "网关离线，无法操作");
        }
        if (!messageBaseService.checkMsgConsumeFinish(SubMsgType.CHANNEL_ADD_OR_DELETE_STATE, Set.of(channelId))){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "存在应用在使用该数据，请稍后再删除");
        }
        channelMapper.deleteById(channelId);
        CommonResponse<?> commonResponse = parsingEngineApi.customEvent(new DeviceControlReq(channelId, IdType.CHANNEL, MsgType.CHANNEL_DELETE_HARD, 1500L));
        if (commonResponse.isError()){
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "通道北向服务", "通道硬删除失败", commonResponse.getData(), commonResponse.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, commonResponse.getMsg());
        }
    }

    /**
     * 根据设备id删除通道
     * @param deviceId 设备id
     * @param isDeleteData 是否删除数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void channelDeleteByDeviceId(Long deviceId, Boolean isDeleteData) {
        List<ChannelInfo> channelInfoList = channelMapper.selectByDeviceId(deviceId);
        if (channelInfoList.isEmpty()) {
            return;
        }
        if (isDeleteData) {
            List<Long> channelInfoIdList = channelInfoList.stream().map(ChannelInfo::getId).collect(Collectors.toList());
            if (!messageBaseService.checkMsgConsumeFinish(SubMsgType.CHANNEL_ADD_OR_DELETE_STATE, new HashSet<>(channelInfoIdList))){
                throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "存在应用使用该设备下的通道，请稍后重试");
            }
            if (!channelInfoIdList.isEmpty()) {
                detailMapper.deleteByDcIdsAndType(channelInfoIdList, DetailType.CHANNEL.getCode());
            }
            channelMapper.deleteByDeviceId(deviceId);
        } else {
            LocalDateTime nowTime = LocalDateTime.now();
            channelInfoList.forEach(channelInfo -> {
                channelInfo.setSignState(SignState.DELETED.getCode());
                channelInfo.setUpdateTime(nowTime);
            });
            channelMapper.batchUpdateSignState(channelInfoList);
            messageBaseService.msgDistribute(SubMsgType.CHANNEL_ADD_OR_DELETE_STATE, channelInfoList.stream().collect(Collectors.toMap(ChannelInfo::getId, JSONObject::toJSONString)));
        }


    }


    @Override
    public VideoRecordRsp channelRecord(Long channelId, LocalDateTime startTime, LocalDateTime endTime) {
        getChannelInfoAndValid(channelId);
        DeviceControlReq deviceReq = new DeviceControlReq(channelId, IdType.CHANNEL, MsgType.CHANNEL_RECORD_INFO, 15000L);
        deviceReq.putData(StandardName.COM_START_TIME, DateUtils.DATE_TIME_FORMATTER.format(startTime));
        deviceReq.putData(StandardName.COM_END_TIME, DateUtils.DATE_TIME_FORMATTER.format(endTime));
        CommonResponse<?> response = parsingEngineApi.customEvent(deviceReq);
        if (response.isError()) {
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备播放北向服务", "视频录像数据获取失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        return JSONObject.parseObject(JSONObject.toJSONString(response.getData()), VideoRecordRsp.class);
    }


    private ChannelInfo getChannelInfoAndValid(Long chId) {
        ChannelInfo channelInfo = dataBaseService.getChannelInfo(chId);
        // 检测通道是否在线
        if (channelInfo.getOnlineState().equals(CommonEnum.DISABLE.getCode())) {
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "通道处于离线状态");
        }
        // 检测通道是否注册成功
        if (!channelInfo.getSignState().equals(SignState.SUCCESS.getCode())) {
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "通道未注册成功");
        }
        return channelInfo;
    }

    /**
     * 云台控制状态
     * @param channelId     通道ID
     * @param cmdCode  指令code
     * @param valueMap  值map
     */
    @Override
    public void channelPtzControl(Long channelId, Integer cmdCode, Integer cmdValue, Map<String, Object> valueMap) {
        getChannelInfoAndValid(channelId);
        DeviceControlReq req = new DeviceControlReq(channelId, IdType.CHANNEL, MsgType.CHANNEL_PTZ_CONTROL, 15000L);
        req.putData(StandardName.PTZ_CMD_CODE, cmdCode);
        req.putData(StandardName.PTZ_CMD_VALUE, cmdValue);
        if (Objects.nonNull(valueMap) && !valueMap.isEmpty()){
            req.putAllData(valueMap);
        }
        CommonResponse<?> response = parsingEngineApi.customEvent(req);
        if (response.isError()) {
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "云台控制北向服务", "云台控制失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
    }

    /**
     * 预置位查询
     * @param channelId 通道id
     * @return
     */
    @Override
    public List<PtzPresetRsp> channelPtzPresetGet(Long channelId) {
        getChannelInfoAndValid(channelId);
        DeviceControlReq req = new DeviceControlReq(channelId, IdType.CHANNEL, MsgType.CHANNEL_PTZ_PRESET, 15000L);
        CommonResponse<?> response = parsingEngineApi.customEvent(req);
        if (response.isError()) {
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "云台控制北向服务", "预置位查询失败", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
        if (Objects.isNull(response.getData())){
            return Collections.EMPTY_LIST;
        }
        return JSONArray.parseArray(JSONArray.toJSONString(response.getData())).toList(PtzPresetRsp.class);
    }

    /**
     * 3D控制
     * @param channelId 通道id
     * @param dragType 放大-1 缩小-2
     * @param length 拉宽长度
     * @param width 拉宽宽度
     * @param midPointX 拉框中心的横轴坐标像素值
     * @param midPointY 拉框中心的纵轴坐标像素值
     * @param lengthX 拉框长度像素值
     * @param lengthY 拉框宽度像素值
     */
    @Override
    public void channelPtz3d(Long channelId, Integer dragType, Integer length, Integer width, Integer midPointX, Integer midPointY, Integer lengthX, Integer lengthY) {
        getChannelInfoAndValid(channelId);
        DeviceControlReq req = new DeviceControlReq(channelId, IdType.CHANNEL, MsgType.CHANNEL_PTZ_3D, 15000L);
        req.putData(StandardName.PTZ_3D_DRAG_TYPE, dragType);
        req.putData(StandardName.PTZ_3D_LENGTH, length);
        req.putData(StandardName.PTZ_3D_WIDTH, width);
        req.putData(StandardName.PTZ_3D_POINT_X, midPointX);
        req.putData(StandardName.PTZ_3D_POINT_Y, midPointY);
        req.putData(StandardName.PTZ_3D_LENGTH_X, lengthX);
        req.putData(StandardName.PTZ_3D_LENGTH_Y, lengthY);
        CommonResponse<?> response = parsingEngineApi.customEvent(req);
        if (response.isError()) {
            log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "云台控制北向服务", "3D控制", response.getData(), response.getMsg());
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, response.getMsg());
        }
    }

    @Override
    public Set<Long> channelDeployAndWithdrawDefenses(List<Long> channelIdList, Boolean isDeploy) {
        List<ChannelInfo> channelInfoList = channelMapper.selectByIds(channelIdList);
        log.warn("云台控制北向服务，通道集合[{}]", channelInfoList);
        if (channelInfoList.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("未找到通道集合[%s]", channelIdList));
        }
        Map<Long, List<Long>> deviceChannelIdMap = channelInfoList
                .stream().collect(Collectors.groupingBy(ChannelInfo::getDeviceId, Collectors.mapping(ChannelInfo::getId, Collectors.toList())));
        List<DeviceInfo> deviceInfoList = deviceMapper.selectByIds(deviceChannelIdMap.keySet());
        log.warn("云台控制北向服务，设备集合[{}]", deviceInfoList);
        if (deviceInfoList.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("未找到设备集合[%s]", deviceInfoList));
        }
        Map<Long, List<Long>> gatewayDeviceIdMap = deviceInfoList
                .stream().collect(Collectors.groupingBy(DeviceInfo::getGatewayId, Collectors.mapping(DeviceInfo::getId, Collectors.toList())));
        MsgType msgType =  isDeploy ? MsgType.CHANNEL_DEFENSES_DEPLOY : MsgType.CHANNEL_DEFENSES_WITHDRAW;
        Set<Long> failureChannelSet = new HashSet<>(channelIdList.size());
        for (Map.Entry<Long, List<Long>> entry : gatewayDeviceIdMap.entrySet()){
            DeviceControlReq deviceControlReq = new DeviceControlReq(entry.getKey(), IdType.GATEWAY, msgType, 15000L);
            List<Long> channelIds = new ArrayList<>();
            for (Long deviceId : entry.getValue()){
                List<Long> deviceChannelIdList = deviceChannelIdMap.get(deviceId);
                channelIds.addAll(deviceChannelIdList);
                deviceControlReq.putData(String.valueOf(deviceId), channelIdList);
            }
            try {
                CommonResponse<?> response = parsingEngineApi.customEvent(deviceControlReq);
                if (response.isError()) {
                    log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "通道北向服务", "设备布防与撤防失败", String.format("请求体%s", deviceControlReq), String.format("%s:%s", response.getMsg(), response.getData()));
                    failureChannelSet.addAll(channelIds);
                }
                if (Objects.nonNull(response.getData())){
                    List<Long> failureChannelList = JSONArray.parseArray(response.getData().toString()).toList(Long.class);
                    if (!failureChannelList.isEmpty()){
                        failureChannelSet.addAll(failureChannelList);
                    }
                }
            }catch (Exception ex){
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "通道北向服务", "设备布防与撤防失败", String.format("请求体%s", deviceControlReq), ex);
                failureChannelSet.addAll(channelIds);
            }
        }
        return failureChannelSet;
    }

}
