package com.runjian.parsing.protocol.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.shaded.com.google.gson.JsonObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.dao.ChannelMapper;
import com.runjian.parsing.dao.DeviceMapper;
import com.runjian.parsing.entity.DeviceInfo;
import com.runjian.parsing.entity.TaskInfo;
import com.runjian.parsing.protocol.SouthProtocol;
import com.runjian.parsing.service.DataBaseService;
import com.runjian.parsing.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/1/28 15:45
 */
@Service
public class DefaultSouthProtocol implements SouthProtocol {

    @Autowired
    private TaskService taskService;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private DataBaseService dataBaseService;


    /**
     * 默认设备Id名称
     */
    protected String deviceIdName = "deviceId";

    /**
     * 默认通道Id名称
     */
    protected String channelIdName = "channelId";

    /**
     * 默认结果名字
     */
    protected String resultName = "result";

    @Override
    public String getProtocolName() {
        return DEFAULT_PROTOCOL;
    }

    @Override
    public void deviceSignIn(Object data) {

    }

    @Override
    public void deviceSync(Long taskId, Object data) {
        DeferredResult<CommonResponse<?>> deferredResult = taskService.removeDeferredResult(taskId);
        deferredResult.setResult(CommonResponse.success(data));
        taskService.taskSuccess(taskId);
    }

    @Override
    public void deviceAdd(Long taskId, Object data) {

        TaskInfo taskInfo = taskService.getTask(taskId);
        if (!taskInfo.getTaskState().equals(TaskState.RUNNING.getCode())){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "该任务状态异常");
        }
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


    @Override
    public void deviceDelete(Long taskId, Object data) {
        TaskInfo taskInfo = taskService.getTask(taskId);
        if (!taskInfo.getTaskState().equals(TaskState.RUNNING.getCode())){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "该任务状态异常");
        }
        if (Objects.isNull(data)){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "设备id为空");
        }
        Boolean isSuccess = (Boolean)data;
        if (isSuccess){
            channelMapper.deleteByDeviceId(taskInfo.getDeviceId());
            deviceMapper.deleteById(taskInfo.getDeviceId());
        }
        taskService.removeDeferredResult(taskId).setResult(CommonResponse.success(isSuccess));
        taskService.taskSuccess(taskId);
    }

    @Override
    public void channelSync(Long taskId, Object data) {
        JSONArray objects = JSON.parseArray(data.toString());

        if (objects.size() == 0){
            throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "结果为空");
        }
        TaskInfo taskInfo = taskService.getTask(taskId);
        if (!taskInfo.getTaskState().equals(TaskState.RUNNING.getCode())){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "该任务状态异常");
        }
        for (int i = 0; i < objects.size(); i++){
            JSONObject jsonObject = objects.getJSONObject(i);
            String channelOriginId = jsonObject.getString(channelIdName);
            // todo 转换数据
        }


        taskService.removeDeferredResult(taskId).setResult(CommonResponse.success(data));
        taskService.taskSuccess(taskId);

    }

    @Override
    public void channelPlay(Long taskId, Object dataMap) {
        customEvent(taskId, dataMap);
    }

    @Override
    public void getChannelRecord(Long taskId, Object dataMap) {
        customEvent(taskId, dataMap);
    }

    @Override
    public void channelPlayback(Long taskId, Object dataMap) {
        customEvent(taskId, dataMap);
    }

    @Override
    public void customEvent(Long taskId, Object dataMap) {
        TaskInfo taskInfo = taskService.getTask(taskId);
        if (!taskInfo.getTaskState().equals(TaskState.RUNNING.getCode())){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, "该任务状态异常");
        }
        taskService.removeDeferredResult(taskId).setResult(CommonResponse.success(dataMap));
        taskService.taskSuccess(taskId);
    }
}
