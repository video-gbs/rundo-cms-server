package com.runjian.parsing.service.common.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.MqConstant;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.dao.StreamTaskMapper;
import com.runjian.parsing.entity.DispatchInfo;
import com.runjian.parsing.entity.StreamTaskInfo;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.mq.listener.MqDefaultProperties;
import com.runjian.parsing.mq.listener.MqListenerConfig;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.service.common.StreamTaskService;
import com.runjian.parsing.vo.CommonMqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Miracle
 * @date 2023/2/10 11:31
 */
@Service
public class StreamTaskServiceImpl implements StreamTaskService {

    @Autowired
    private StreamTaskMapper streamTaskMapper;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    private MqDefaultProperties mqDefaultProperties;

    @Autowired
    private DataBaseService dataBaseService;

    private static final String OUT_TIME = "OUT_TIME";

    @Override
    public void sendMsgToGateway(Long dispatchId, Long channelId, String streamId, String msgType, Object data, DeferredResult<CommonResponse<?>> response) {
        DispatchInfo dispatchInfo = dataBaseService.getDispatchInfo(dispatchId);
        String mqId = UUID.randomUUID().toString().replace("-", "");
        Long taskId;
        if (Objects.isNull(response)){
            taskId = createTask(dispatchId, channelId, streamId, mqId, msgType, TaskState.SUCCESS);
        }else {
            taskId = createAsyncTask(dispatchId, channelId, streamId, mqId, msgType, response);
        }
        String mqKey = MqConstant.STREAM_PREFIX + MqConstant.GET_SET_PREFIX + dispatchId;
        CommonMqDto<Object> request = new CommonMqDto<>(dispatchInfo.getSerialNum(), msgType, taskId.toString(), LocalDateTime.now());
        request.setData(data);
        rabbitMqSender.sendMsgByRoutingKey(mqDefaultProperties.getStreamExchangeId(), mqKey, mqId, request, true);
    }

    @Override
    public Long createAsyncTask(Long dispatchId, Long channelId, String streamId, String mqId, String msgType, DeferredResult<CommonResponse<?>> deferredResult) {
        Long taskId = createTask(dispatchId, channelId, streamId, mqId, msgType, TaskState.RUNNING);
        asynReqMap.put(taskId, deferredResult);
        deferredResult.onTimeout(() -> {
            deferredResult.setResult(CommonResponse.failure(BusinessErrorEnums.FEIGN_REQUEST_TIME_OUT));
            asynReqMap.remove(taskId);
            taskError(taskId, BusinessErrorEnums.VALID_REQUEST_TIME_OUT, OUT_TIME);
        });
        return taskId;
    }


    @Override
    public Long createTask(Long dispatchId, Long channelId, String streamId, String mqId, String msgType, TaskState taskState) {
        LocalDateTime nowTime = LocalDateTime.now();
        StreamTaskInfo streamTaskInfo = new StreamTaskInfo();
        streamTaskInfo.setDispatchId(dispatchId);
        streamTaskInfo.setChannelId(channelId);
        streamTaskInfo.setStreamId(streamId);
        streamTaskInfo.setMqId(mqId);
        streamTaskInfo.setMsgType(msgType);
        streamTaskInfo.setCreateTime(nowTime);
        streamTaskInfo.setUpdateTime(nowTime);
        streamTaskInfo.setState(taskState.getCode());
        streamTaskMapper.save(streamTaskInfo);
        return streamTaskInfo.getId();
    }

    @Override
    public StreamTaskInfo getTask(Long taskId) {
        Optional<StreamTaskInfo> taskInfoOp = streamTaskMapper.selectById(taskId);
        return taskInfoOp.orElse(null);
    }

    @Override
    public StreamTaskInfo getTaskValid(Long taskId, TaskState taskState) {
        Optional<StreamTaskInfo> taskInfoOp = streamTaskMapper.selectById(taskId);
        if (taskInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("任务%s不存在", taskId));
        }
        StreamTaskInfo streamTaskInfo = taskInfoOp.get();
        if (!streamTaskInfo.getState().equals(taskState.getCode())){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, String.format("任务状态异常，当前任务状态：%s", TaskState.getMsg(streamTaskInfo.getState())));
        }
        return streamTaskInfo;
    }

    @Override
    public DeferredResult removeDeferredResult(Long taskId, TaskState taskState, String detail) {
        streamTaskMapper.updateState(taskId, taskState.getCode(), detail, LocalDateTime.now());
        return asynReqMap.remove(taskId);
    }

    @Override
    public void taskSuccess(Long taskId, Object data) {
        streamTaskMapper.updateState(taskId, TaskState.SUCCESS.getCode(), null, LocalDateTime.now());
        DeferredResult deferredResult = asynReqMap.remove(taskId);
        deferredResult.setResult(CommonResponse.success(data));
    }

    @Override
    public void taskError(Long taskId, BusinessErrorEnums errorEnums, String detail) {
        streamTaskMapper.updateState(taskId, TaskState.SUCCESS.getCode(), detail, LocalDateTime.now());
        DeferredResult deferredResult = asynReqMap.remove(taskId);
        deferredResult.setResult(CommonResponse.failure(errorEnums, detail));

    }
}