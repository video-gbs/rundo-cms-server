package com.runjian.parsing.service.protocol.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.StandardName;
import com.runjian.common.constant.MsgType;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.entity.ChannelInfo;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.entity.GatewayTaskInfo;
import com.runjian.parsing.feign.DeviceControlApi;
import com.runjian.parsing.service.common.GatewayTaskService;
import com.runjian.parsing.service.protocol.SouthProtocol;
import com.runjian.parsing.vo.CommonMqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/28 15:45
 */
@Slf4j
@Service
@RequiredArgsConstructor
public abstract class AbstractSouthProtocol implements SouthProtocol {

    private final GatewayTaskService gatewayTaskService;

    private final DeviceMapper deviceMapper;

    private final ChannelMapper channelMapper;

    private final DeviceControlApi deviceControlApi;


    /**
     * 消息统一处理分发
     * @param msgType
     * @param taskId
     * @param data
     */
    @Override
    public void msgDistribute(String msgType, Long gatewayId, Long taskId, Object data) {
        switch (MsgType.getByStr(msgType)){
            case DEVICE_SIGN_IN:
                deviceSignIn(gatewayId, data);
                return;
            case DEVICE_TOTAL_SYNC:
                deviceBatchSignIn(gatewayId, data);
                return;
            case DEVICE_SYNC:
                deviceSync(taskId, data);
                return;
            case DEVICE_ADD:
                deviceAdd(taskId, data);
                return;
            case DEVICE_DELETE:
                deviceDelete(taskId, data);
                return;
            case CHANNEL_SYNC:
                channelSync(taskId, data);
                return;
            default:
                customEvent(taskId, data);
        }
    }

    @Override
    public void customEvent(Long taskId, Object dataMap) {
        if (Objects.isNull(taskId)){
            return;
        }
        gatewayTaskService.getTaskValid(taskId, TaskState.RUNNING);
        gatewayTaskService.taskSuccess(taskId, dataMap);
    }

    @Override
    public void errorEvent(Long taskId, CommonMqDto<?> response) {
        log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "网关南向信息处理服务", "网关异常消息记录", response.getMsgType(), response);
        if (Objects.nonNull(taskId)){
            gatewayTaskService.getTaskValid(taskId, TaskState.RUNNING);
            gatewayTaskService.removeDeferredResult(taskId, TaskState.ERROR, response.getMsg()).setResult(response);
        }
        deviceControlApi.errorEvent(response);
    }

    /**
     * 设备注册
     * @param gatewayId 网关id
     * @param data      数据集合
     */
    public void deviceSignIn(Long gatewayId, Object data) {
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "data为空");
        }
        JSONObject jsonObject = saveDevice(deviceSignInConvert(JSONObject.parseObject(data.toString())), gatewayId);
        CommonResponse<?> commonResponse = deviceControlApi.deviceSignIn(jsonObject);
        if (commonResponse.getCode() != 0) {
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, commonResponse.getMsg());
        }
    }

    public void deviceBatchSignIn(Long gatewayId, Object data) {
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "data为空");
        }
        JSONArray jsonArray = JSONArray.parseArray(data.toString());
        for (int i = 0; i < jsonArray.size(); i++) {
            saveDevice(deviceBatchSignInConvert(jsonArray.getJSONObject(i)), gatewayId);
        }
        CommonResponse<?> commonResponse = deviceControlApi.deviceBatchSignIn(jsonArray);
        if (commonResponse.getCode() != 0) {
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, commonResponse.getMsg());
        }
    }


    /**
     * 设备同步
     * @param taskId 任务id
     * @param data   数据集合
     */
    public void deviceSync(Long taskId, Object data) {
        if (Objects.isNull(data)) {
            gatewayTaskService.taskError(taskId, BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "设备id为空");
            return;
        }
        GatewayTaskInfo gatewayTaskInfo = gatewayTaskService.getTaskValid(taskId, TaskState.RUNNING);
        JSONObject jsonObject = deviceSyncConvert(JSON.parseObject(data.toString()));
        jsonObject.put(StandardName.DEVICE_ID, gatewayTaskInfo.getDeviceId());
        customEvent(taskId, data);
    }



    /**
     * 设备添加，网关返回设备原始id
     *
     * @param taskId 任务id
     * @param data   数据集合
     */
    public void deviceAdd(Long taskId, Object data) {
        if (Objects.isNull(data)) {
            gatewayTaskService.taskError(taskId, BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "设备id为空");
            return;
        }
        GatewayTaskInfo gatewayTaskInfo = gatewayTaskService.getTaskValid(taskId, TaskState.RUNNING);
        String deviceOriginId = data.toString();
        LocalDateTime nowTime = LocalDateTime.now();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setOriginId(deviceOriginId);
        deviceInfo.setGatewayId(gatewayTaskInfo.getGatewayId());
        deviceInfo.setCreateTime(nowTime);
        deviceInfo.setUpdateTime(nowTime);
        deviceMapper.save(deviceInfo);
        customEvent(taskId, deviceInfo.getId());
    }


    /**
     * 设备删除，网关需返回boolean类型表示删除成功与否
     *
     * @param taskId 任务id
     * @param data   数据集合
     */
    public void deviceDelete(Long taskId, Object data) {
        if (Objects.isNull(data)) {
            gatewayTaskService.taskError(taskId, BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "设备id为空");
            return;
        }
        GatewayTaskInfo gatewayTaskInfo = gatewayTaskService.getTaskValid(taskId, TaskState.RUNNING);
        Boolean isSuccess = (Boolean) data;
        if (isSuccess) {
            channelMapper.deleteByDeviceId(gatewayTaskInfo.getDeviceId());
            deviceMapper.deleteById(gatewayTaskInfo.getDeviceId());
        }
        customEvent(taskId, isSuccess);
    }

    /**
     * 设备同步
     *
     * @param taskId 任务id
     * @param data   数据集合
     */
    public void channelSync(Long taskId, Object data) {
        if (Objects.isNull(data)) {
            gatewayTaskService.taskError(taskId, BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "返回数据为空");
            return;
        }
        JSONObject jsonData = JSON.parseObject(data.toString());
        JSONArray objects = jsonData.getJSONArray(StandardName.CHANNEL_SYNC_LIST);
        GatewayTaskInfo gatewayTaskInfo = gatewayTaskService.getTaskValid(taskId, TaskState.RUNNING);
        jsonData.put(StandardName.DEVICE_ID, gatewayTaskInfo.getDeviceId());
        List<ChannelInfo> channelInfoList = new ArrayList<>(objects.size());
        // todo 加上设备id的锁
        for (int i = 0; i < objects.size(); i++) {
            // 转换数据
            JSONObject jsonObject = channelSyncConvert(objects.getJSONObject(i));
            String channelOriginId = jsonObject.getString(StandardName.ORIGIN_ID);
            // 校验数据是否已存在
            Optional<ChannelInfo> channelInfoOp = channelMapper.selectByDeviceIdAndOriginId(gatewayTaskInfo.getDeviceId(), channelOriginId);
            ChannelInfo channelInfo = channelInfoOp.orElseGet(ChannelInfo::new);
            if (channelInfoOp.isEmpty()) {
                // 创建新的数据
                LocalDateTime nowTime = LocalDateTime.now();
                channelInfo.setOriginId(channelOriginId);
                channelInfo.setDeviceId(gatewayTaskInfo.getDeviceId());
                channelInfo.setCreateTime(nowTime);
                channelInfo.setUpdateTime(nowTime);
                channelInfoList.add(channelInfo);
            }
            jsonObject.put(StandardName.DEVICE_ID, channelInfo.getDeviceId());
            jsonObject.put(StandardName.CHANNEL_ID, channelInfo.getId());
        }
        if (channelInfoList.size() > 0){
            channelMapper.batchSave(channelInfoList);
        }
        customEvent(taskId, objects);
    }

    /**
     * 保存设备
     * @param jsonObject
     * @param gatewayId
     * @return
     */
    protected JSONObject saveDevice(JSONObject jsonObject, Long gatewayId) {
        String deviceOriginId = jsonObject.getString(StandardName.ORIGIN_ID);
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectByGatewayIdAndOriginId(gatewayId, deviceOriginId);
        DeviceInfo deviceInfo = deviceInfoOp.orElseGet(DeviceInfo::new);
        if (deviceInfoOp.isEmpty()) {
            LocalDateTime nowTime = LocalDateTime.now();
            deviceInfo.setOriginId(deviceOriginId);
            deviceInfo.setGatewayId(gatewayId);
            deviceInfo.setUpdateTime(nowTime);
            deviceInfo.setCreateTime(nowTime);
            deviceMapper.save(deviceInfo);
        }
        jsonObject.put(StandardName.DEVICE_ID, deviceInfo.getId());
        jsonObject.put(StandardName.GATEWAY_ID, gatewayId);
        return jsonObject;
    }

    /**
     * 设备注册数据转换，必须转换原始id
     * @param jsonObject
     * @return
     */
    protected abstract JSONObject deviceSignInConvert(JSONObject jsonObject);

    /**
     * 设备批量注册数据转换，必须转换原始id
     * @param jsonObject
     * @return
     */
    protected abstract JSONObject deviceBatchSignInConvert(JSONObject jsonObject);

    /**
     * 设备同步数据转换，必须转换原始id
     * @param jsonObject
     * @return
     */
    protected abstract JSONObject deviceSyncConvert(JSONObject jsonObject);

    /**
     * 通道同步数据转换，必须转换原始id
     * @param jsonObject
     * @return
     */
    protected abstract JSONObject channelSyncConvert(JSONObject jsonObject);

}
