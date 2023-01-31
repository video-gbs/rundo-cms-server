package com.runjian.parsing.protocol.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.entity.ChannelInfo;
import com.runjian.parsing.entity.TaskInfo;
import com.runjian.parsing.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Miracle
 * @date 2023/1/31 10:46
 */
@Service
public class Gb28181SouthProtocol extends DefaultSouthProtocol {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ChannelMapper channelMapper;

    @Getter
    @AllArgsConstructor
    private enum MapData{

        DEVICE_ONLINE_STATE("online", "onlineState"),
        CHANNEL_ONLINE_STATE("status", "onlineState"),
        CHANNEL_NAME("channelName", "name"),
        CHANNEL_TYPE(null, "channelType"),
        CHANNEL_IP("ipAddress", "ip");


        private String originName;

        private String standardName;
    }

    @Override
    public void deviceSignIn(Long gatewayId, Object data) {
        JSONObject jsonObject = convertOnlineState(data);
        super.deviceSignIn(gatewayId,  jsonObject);
    }


    private JSONObject convertOnlineState(Object data) {
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "结果为空");
        }
        JSONObject jsonObject = JSON.parseObject(data.toString());
        int onlineState = jsonObject.getIntValue(MapData.DEVICE_ONLINE_STATE.originName);
        jsonObject.put(MapData.DEVICE_ONLINE_STATE.standardName, onlineState);
        return jsonObject;
    }


    /**
     * 设备同步
     * @param taskId 任务id
     * @param data 数据集合
     */
    @Override
    public void deviceSync(Long taskId, Object data) {
        JSONObject jsonObject = convertOnlineState(data);
        super.deviceSync(taskId, jsonObject);
    }

    @Override
    public void channelSync(Long taskId, Object data) {
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "结果为空");
        }
        JSONObject jsonData = JSON.parseObject(data.toString());
        JSONArray objects = jsonData.getJSONArray(CHANNEL_SYNC_LIST);
        TaskInfo taskInfo = taskService.getTaskValid(taskId, TaskState.RUNNING);
        jsonData.put(DEVICE_ID, taskInfo.getDeviceId());
        if (Objects.nonNull(objects) && objects.size() > 0){
            for (int i = 0; i < objects.size(); i++){
                JSONObject jsonObject = objects.getJSONObject(i);
                String channelOriginId = jsonObject.getString(CHANNEL_ID);
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
                int onlineState = jsonObject.getIntValue(MapData.CHANNEL_ONLINE_STATE.originName);
                String channelName = jsonObject.getString(MapData.CHANNEL_NAME.originName);
                String channelIp = jsonObject.getString(MapData.CHANNEL_IP.originName);
                jsonObject.put(MapData.CHANNEL_TYPE.standardName, 5);
                jsonObject.put(MapData.CHANNEL_ONLINE_STATE.standardName, onlineState);
                jsonObject.put(CHANNEL_ID, channelInfo.getId());
                jsonObject.put(MapData.CHANNEL_NAME.standardName, channelName);
                jsonObject.put(MapData.CHANNEL_IP.standardName, channelIp);
            }
        }
        taskService.removeDeferredResult(taskId).setResult(CommonResponse.success(jsonData));
        taskService.taskSuccess(taskId);
    }
}
