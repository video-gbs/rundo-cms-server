package com.runjian.parsing.protocol.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.StandardName;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.entity.ChannelInfo;
import com.runjian.parsing.entity.TaskInfo;
import com.runjian.parsing.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    private final static String IP = "ipAddress";

    private final static String DEVICE_ONLINE_STATE = "online";

    private final static String CHANNEL_ONLINE_STATE = "status";

    private final static String CHANNEL_NAME = "channelName";


    /**
     * 重写设备注册
     * @param gatewayId 网关id
     * @param data 数据集合
     */
    @Override
    public void deviceSignIn(Long gatewayId, Object data) {
        JSONObject jsonObject = convertOnlineState(data);
        super.deviceSignIn(gatewayId,  jsonObject);
    }

    /**
     * 重写设备同步
     * @param taskId 任务id
     * @param data 数据集合
     */
    @Override
    public void deviceSync(Long taskId, Object data) {
        JSONObject jsonObject = convertOnlineState(data);
        super.deviceSync(taskId, jsonObject);
    }

    /**
     * 重写通道同步
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
        jsonData.put(StandardName.DEVICE_ID, taskInfo.getDeviceId());
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
                int onlineState = jsonObject.getIntValue(CHANNEL_ONLINE_STATE);
                String channelName = jsonObject.getString(CHANNEL_NAME);
                String channelIp = jsonObject.getString(IP);
                jsonObject.put(StandardName.CHANNEL_TYPE, 5);
                jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
                jsonObject.put(StandardName.CHANNEL_ID, channelInfo.getId());
                jsonObject.put(StandardName.COM_NAME, channelName);
                jsonObject.put(StandardName.COM_IP, channelIp);
                jsonObject.put(StandardName.ORIGIN_ID, channelInfo.getOriginId());
            }
        }
        taskService.removeDeferredResult(taskId).setResult(CommonResponse.success(jsonData));
        taskService.taskSuccess(taskId);
    }

    /**
     * 转换在线状态
     * @param data 传输数据
     * @return
     */
    private JSONObject convertOnlineState(Object data) {
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "结果为空");
        }
        JSONObject jsonObject = JSON.parseObject(data.toString());
        int onlineState = jsonObject.getIntValue(DEVICE_ONLINE_STATE);
        jsonObject.put(StandardName.COM_ONLINE_STATE, onlineState);
        return jsonObject;
    }
}