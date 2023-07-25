package com.runjian.parsing.service.protocol;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
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
import com.runjian.parsing.vo.CommonMqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Miracle
 * @date 2023/1/28 15:45
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractSouthProtocol implements SouthProtocol {

    protected final GatewayTaskService gatewayTaskService;

    protected final DeviceMapper deviceMapper;

    protected final ChannelMapper channelMapper;

    protected final DeviceControlApi deviceControlApi;

    protected final RedissonClient redissonClient;

    protected final DataSourceTransactionManager dataSourceTransactionManager;

    protected final TransactionDefinition transactionDefinition;

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
            case DEVICE_DELETE_HARD:
                deviceDeleteHard(taskId, data);
                return;
            case CHANNEL_SYNC:
                channelSync(taskId, data);
                return;
            case CHANNEL_DELETE_HARD:
                channelDeleteSoft(taskId, data);
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
        JSONObject jsonObject = deviceSignInConvert(JSONObject.parseObject(data.toString()));
        DeviceInfo deviceInfo = saveDeviceInfo(jsonObject, gatewayId);
        jsonObject.put(StandardName.DEVICE_ID, deviceInfo.getId());
        jsonObject.put(StandardName.GATEWAY_ID, gatewayId);
        CommonResponse<?> commonResponse = deviceControlApi.deviceSignIn(jsonObject);
        if (commonResponse.isError()) {
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, commonResponse.getMsg());
        }
    }

    /**
     * 设备批量注册
     * @param gatewayId
     * @param data
     */
    public void deviceBatchSignIn(Long gatewayId, Object data) {
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "data为空");
        }
        JSONArray jsonArray = JSONArray.parseArray(data.toString());
        if (jsonArray.size() == 0){
            return;
        }
        RLock lock = redissonClient.getLock(MarkConstant.REDIS_DEVICE_BATCH_SIGN_IN_LOCK + gatewayId);
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try{
            lock.lock(15, TimeUnit.SECONDS);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = deviceBatchSignInConvert(jsonArray.getJSONObject(i));
                DeviceInfo deviceInfo = saveDeviceInfo(jsonObject, gatewayId);
                jsonObject.put(StandardName.DEVICE_ID, deviceInfo.getId());
                jsonObject.put(StandardName.GATEWAY_ID, gatewayId);
            }
            dataSourceTransactionManager.commit(transactionStatus);
        } catch (Exception ex){
            dataSourceTransactionManager.rollback(transactionStatus);
            throw ex;
        }finally {
            lock.unlock();
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
        Optional<DeviceInfo> deviceInfoOp = deviceMapper.selectByGatewayIdAndOriginId(gatewayTaskInfo.getGatewayId(), deviceOriginId);
        DeviceInfo deviceInfo = deviceInfoOp.orElseGet(DeviceInfo::new);
        if (deviceInfoOp.isEmpty()){
            deviceInfo.setOriginId(deviceOriginId);
            deviceInfo.setGatewayId(gatewayTaskInfo.getGatewayId());
            deviceInfo.setCreateTime(nowTime);
            deviceInfo.setUpdateTime(nowTime);
            deviceMapper.save(deviceInfo);
        }
        customEvent(taskId, deviceInfo.getId());
    }


    /**
     * 设备删除，网关需返回boolean类型表示删除成功与否
     *
     * @param taskId 任务id
     * @param data   数据集合
     */
    public void deviceDeleteHard(Long taskId, Object data) {
        if (Objects.isNull(data)) {
            gatewayTaskService.taskError(taskId, BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "设备id为空");
            return;
        }
        GatewayTaskInfo gatewayTaskInfo = gatewayTaskService.getTaskValid(taskId, TaskState.RUNNING);
        Boolean isSuccess = (Boolean) data;
        if (isSuccess) {
            TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            try{
                channelMapper.deleteByDeviceId(gatewayTaskInfo.getDeviceId());
                deviceMapper.deleteById(gatewayTaskInfo.getDeviceId());
                dataSourceTransactionManager.commit(transactionStatus);
            }catch (Exception ex){
                dataSourceTransactionManager.rollback(transactionStatus);
                throw ex;
            }
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
        JSONObject jsonData = JSONObject.parseObject(JSONObject.toJSONString(data));
        JSONArray objects = jsonData.getJSONArray(StandardName.CHANNEL_SYNC_LIST);
        GatewayTaskInfo gatewayTaskInfo = gatewayTaskService.getTaskValid(taskId, TaskState.RUNNING);
        jsonData.put(StandardName.DEVICE_ID, gatewayTaskInfo.getDeviceId());
        RLock lock = redissonClient.getLock(MarkConstant.REDIS_CHANNEL_SYNC_LOCK + gatewayTaskInfo.getDeviceId());
        try{
            lock.lock(15, TimeUnit.SECONDS);
            List<ChannelInfo> channelInfoList = new ArrayList<>(objects.size());
            List<Integer> addObjectIndex = new ArrayList<>(objects.size());
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
                    addObjectIndex.add(i);
                }else {
                    jsonObject.put(StandardName.CHANNEL_ID, channelInfo.getId());
                }
                jsonObject.put(StandardName.DEVICE_ID, channelInfo.getDeviceId());
            }
            if (channelInfoList.size() > 0){
                TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
                try{
                    channelMapper.batchSave(channelInfoList);
                    for (int i = 0; i < addObjectIndex.size(); i++){
                        JSONObject jsonObject = objects.getJSONObject(addObjectIndex.get(i));
                        jsonObject.put(StandardName.CHANNEL_ID, channelInfoList.get(i));
                    }
                    dataSourceTransactionManager.commit(transactionStatus);
                }catch (Exception ex){
                    dataSourceTransactionManager.rollback(transactionStatus);
                    throw ex;
                }
            }
        }finally {
            lock.unlock();
        }
        customEvent(taskId, jsonData);
    }

    /**
     * 通道删除
     * @param taskId
     * @param data
     */
    private void channelDeleteSoft(Long taskId, Object data) {
        if (Objects.isNull(data)) {
            gatewayTaskService.taskError(taskId, BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "通道id为空");
            return;
        }
        GatewayTaskInfo gatewayTaskInfo = gatewayTaskService.getTaskValid(taskId, TaskState.RUNNING);
        Boolean isSuccess = (Boolean) data;
        if (isSuccess) {
            TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            try{
                channelMapper.deleteById(gatewayTaskInfo.getChannelId());
                dataSourceTransactionManager.commit(transactionStatus);
            }catch (Exception ex){
                dataSourceTransactionManager.rollback(transactionStatus);
                throw ex;
            }
        }
        customEvent(taskId, isSuccess);
    }

    /**
     * 保存设备
     * @param jsonObject
     * @param gatewayId
     * @return
     */
    protected DeviceInfo saveDeviceInfo(JSONObject jsonObject, Long gatewayId) {
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
        return deviceInfo;
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
