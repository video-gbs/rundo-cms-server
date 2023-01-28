package com.runjian.parsing.service.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.dao.TaskMapper;
import com.runjian.parsing.entity.TaskInfo;
import com.runjian.parsing.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 任务服务
 * @author Miracle
 * @date 2023/1/16 11:37
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public Long createAsyncTask(Long gatewayId, Long deviceId, Long channelId, String clientMsgId, String mqId, String msgType, DeferredResult<CommonResponse<?>> deferredResult) {
        Long taskId = createTask(gatewayId, deviceId, channelId, clientMsgId, mqId, msgType, TaskState.RUNNING, null);
        asynReqMap.put(taskId, deferredResult);
        deferredResult.onTimeout(() -> {
            deferredResult.setResult(CommonResponse.failure(BusinessErrorEnums.FEIGN_REQUEST_TIME_OUT));
            asynReqMap.remove(taskId);
        });
        return taskId;
    }

    @Override
    public Long createTask(Long gatewayId, Long deviceId, Long channelId, String clientMsgId, String mqId, String msgType, TaskState taskState, String desc) {
        LocalDateTime nowTime = LocalDateTime.now();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setGatewayId(gatewayId);
        taskInfo.setDeviceId(deviceId);
        taskInfo.setChannelId(channelId);
        taskInfo.setClientMsgId(clientMsgId);
        taskInfo.setMqId(mqId);
        taskInfo.setMsgType(msgType);
        taskInfo.setCreateTime(nowTime);
        taskInfo.setUpdateTime(nowTime);
        taskInfo.setTaskState(taskState.getCode());
        taskInfo.setDesc(desc);
        taskMapper.save(taskInfo);
        return taskInfo.getId();
    }

    @Override
    public TaskInfo getTask(Long taskId) {
        Optional<TaskInfo> taskInfoOp = taskMapper.selectById(taskId);
        if (taskInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("任务%s不存在", taskId));
        }
        return taskInfoOp.get();
    }

    @Override
    public DeferredResult removeDeferredResult(Long taskId) {
        return asynReqMap.remove(taskId);
    }

    public void updateTaskInfo(Long id, Long deviceId, Long channelId, MsgType msgType, String mqId, String desc){
        Optional<TaskInfo> taskInfoOp = taskMapper.selectById(id);
        if (taskInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("任务%s丢失", id));
        }
        TaskInfo taskInfo = taskInfoOp.get();
        taskInfo.setDeviceId(deviceId);
        taskInfo.setChannelId(channelId);
        taskInfo.setMsgType(msgType.getMsg());
        taskInfo.setDesc(desc);
        taskMapper.update(taskInfo);
    }

    @Override
    public void taskSuccess(Long taskId) {
        taskMapper.updateTaskState(taskId, null);
    }

    @Override
    public void taskError(Long taskId, String detail) {
        taskMapper.updateTaskState(taskId, detail);
    }
}
