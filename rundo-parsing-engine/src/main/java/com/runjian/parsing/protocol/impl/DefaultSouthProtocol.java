package com.runjian.parsing.protocol.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.entity.ChannelInfo;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.entity.TaskInfo;
import com.runjian.parsing.feign.DeviceControlApi;
import com.runjian.parsing.protocol.AbstractSouthProtocol;
import com.runjian.parsing.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/28 15:45
 */
@Service
public class DefaultSouthProtocol extends AbstractSouthProtocol  {

    @Autowired
    private TaskService taskService;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private DeviceControlApi deviceControlApi;

    @Override
    public String getProtocolName() {
        return DEFAULT_PROTOCOL;
    }

    /**
     * 设备注册
     * @param gatewayId 网关id
     * @param data 数据集合
     */
    @Override
    public void deviceSignIn(Long gatewayId, Object data) {
        JSONObject jsonObject = JSON.parseObject(data.toString());
        String deviceOriginId = jsonObject.getString(StandardName.DEVICE_ID);
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectByGatewayIdAndOriginId(gatewayId, deviceOriginId);
        DeviceInfo deviceInfo = deviceInfoOp.orElseGet(DeviceInfo::new);
        if (deviceInfoOp.isEmpty()){
            LocalDateTime nowTime = LocalDateTime.now();
            deviceInfo.setOriginId(deviceOriginId);
            deviceInfo.setGatewayId(gatewayId);
            deviceInfo.setUpdateTime(nowTime);
            deviceInfo.setUpdateTime(nowTime);
            deviceMapper.save(deviceInfo);
        }
        jsonObject.put(StandardName.DEVICE_ID, deviceInfo.getId());
        jsonObject.put(StandardName.GATEWAY_ID, gatewayId);
        CommonResponse<?> commonResponse = deviceControlApi.deviceSignIn(jsonObject);
        if (commonResponse.getCode() != 0){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, commonResponse.getMsg());
        }
    }

    /**
     * 设备同步
     * @param taskId 任务id
     * @param data 数据集合
     */
    @Override
    public void deviceSync(Long taskId, Object data) {
        TaskInfo taskInfo = taskService.getTaskValid(taskId, TaskState.RUNNING);
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "同步数据为空");
        }
        JSONObject jsonObject = JSON.parseObject(data.toString());
        jsonObject.put(StandardName.DEVICE_ID, taskInfo.getDeviceId());
        DeferredResult<CommonResponse<?>> deferredResult = taskService.removeDeferredResult(taskId);
        deferredResult.setResult(CommonResponse.success(jsonObject));
        taskService.taskSuccess(taskId);
    }

    /**
     * 设备添加，网关返回设备原始id
     * @param taskId 任务id
     * @param data 数据集合
     */
    @Override
    public void deviceAdd(Long taskId, Object data) {
        TaskInfo taskInfo = taskService.getTaskValid(taskId, TaskState.RUNNING);
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "设备id为空");
        }
        String deviceOriginId = data.toString();
        LocalDateTime nowTime = LocalDateTime.now();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setOriginId(deviceOriginId);
        deviceInfo.setGatewayId(taskInfo.getGatewayId());
        deviceInfo.setCreateTime(nowTime);
        deviceInfo.setUpdateTime(nowTime);
        deviceMapper.save(deviceInfo);
        taskService.removeDeferredResult(taskId).setResult(CommonResponse.success(deviceInfo.getId()));
        taskService.taskSuccess(taskId);
    }


    /**
     * 设备删除，网关需返回boolean类型表示删除成功与否
     * @param taskId 任务id
     * @param data 数据集合
     */
    @Override
    public void deviceDelete(Long taskId, Object data) {
        TaskInfo taskInfo = taskService.getTaskValid(taskId, TaskState.RUNNING);
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "返回结果为空");
        }
        Boolean isSuccess = (Boolean)data;
        if (isSuccess){
            channelMapper.deleteByDeviceId(taskInfo.getDeviceId());
            deviceMapper.deleteById(taskInfo.getDeviceId());
        }
        taskService.removeDeferredResult(taskId).setResult(CommonResponse.success(isSuccess));
        taskService.taskSuccess(taskId);
    }

    /**
     * 设备同步
     * @param taskId 任务id
     * @param data 数据集合
     */
    @Override
    public void channelSync(Long taskId, Object data) {
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "结果为空");
        }
        JSONObject jsonData = JSON.parseObject(data.toString());
        JSONArray objects = jsonData.getJSONArray(StandardName.CHANNEL_SYNC_LIST);
        TaskInfo taskInfo = taskService.getTaskValid(taskId, TaskState.RUNNING);
        if (Objects.nonNull(objects) && objects.size() > 0){
            for (int i = 0; i < objects.size(); i++){
                JSONObject jsonObject = objects.getJSONObject(i);
                String channelOriginId = jsonObject.getString(StandardName.CHANNEL_ID);
                // 校验数据是否已存在
                Optional<ChannelInfo> channelInfoOp = channelMapper.selectByDeviceIdAndOriginId(taskInfo.getDeviceId(), channelOriginId);
                ChannelInfo channelInfo = channelInfoOp.orElseGet(ChannelInfo::new);
                if (channelInfoOp.isEmpty()){
                    // 创建新的数据
                    LocalDateTime nowTime = LocalDateTime.now();
                    channelInfo.setOriginId(channelOriginId);
                    channelInfo.setDeviceId(taskInfo.getDeviceId());
                    channelInfo.setCreateTime(nowTime);
                    channelInfo.setUpdateTime(nowTime);
                    channelMapper.save(channelInfo);
                }
                // 转换数据
                jsonObject.put(StandardName.DEVICE_ID, taskInfo.getDeviceId());
                jsonObject.put(StandardName.CHANNEL_ID, channelInfo.getId());
            }
        }
        taskService.removeDeferredResult(taskId).setResult(CommonResponse.success(objects));
        taskService.taskSuccess(taskId);
    }



    @Override
    public void commonEvent(Long gatewayId, String msgId, String msgType, Object data) {

        // 判断数据是否存在
        JSONObject jsonData = new JSONObject();

        // 尝试进行数据转换
        if (Objects.nonNull(data)){
            jsonData = JSON.parseObject(data.toString());
            String deviceIdStr = jsonData.getString(StandardName.DEVICE_ID);

            if (Objects.nonNull(deviceIdStr)){
                Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectByGatewayIdAndOriginId(gatewayId, deviceIdStr);
                if (deviceInfoOp.isPresent()){
                    DeviceInfo deviceInfo = deviceInfoOp.get();
                    jsonData.put(StandardName.DEVICE_ID, deviceInfo.getId());
                    String channelIdStr = jsonData.getString(StandardName.CHANNEL_ID);
                    if (Objects.nonNull(channelIdStr)){
                        Optional<ChannelInfo> channelInfoOp = channelMapper.selectByDeviceIdAndOriginId(deviceInfo.getId(), channelIdStr);
                        if (channelInfoOp.isPresent()){
                            ChannelInfo channelInfo = channelInfoOp.get();
                            jsonData.put(StandardName.CHANNEL_ID, channelInfo.getId());
                        }
                    }
                }
            }
        }

        // 将消息类型数据插入
        jsonData.put(StandardName.MSG_TYPE, msgType);
        jsonData.put(StandardName.GATEWAY_ID, gatewayId);

        // 判断是否是北向接口任务
        if (StringUtils.isNumber(msgId)){
            long taskId = Long.parseLong(msgId);
            if (Objects.nonNull(taskService.getTask(taskId))){
                customEvent(taskId, jsonData);
                return;
            }
        }

        // 直接将信息往上层推
        CommonResponse<?> commonResponse = deviceControlApi.commonEvent(jsonData);
        if (commonResponse.getCode() != 0){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, commonResponse.getMsg());
        }
    }

    @Override
    public void customEvent(Long taskId, Object dataMap) {
        taskService.getTaskValid(taskId, TaskState.RUNNING);
        taskService.removeDeferredResult(taskId).setResult(CommonResponse.success(dataMap));
        taskService.taskSuccess(taskId);
    }

    @Override
    public void errorEvent(Long taskId, CommonResponse<?> response) {
        taskService.getTaskValid(taskId, TaskState.RUNNING);
        taskService.removeDeferredResult(taskId).setResult(response);
        taskService.taskError(taskId, response.getMsg());
    }
}
