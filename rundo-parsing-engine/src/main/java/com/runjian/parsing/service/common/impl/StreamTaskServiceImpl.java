package com.runjian.parsing.service.common.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.MqConstant;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.dao.StreamTaskMapper;
import com.runjian.parsing.entity.DispatchInfo;
import com.runjian.parsing.entity.GatewayTaskInfo;
import com.runjian.parsing.entity.StreamTaskInfo;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.mq.listener.MqDefaultProperties;
import com.runjian.parsing.mq.listener.MqListenerConfig;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.service.common.StreamTaskService;
import com.runjian.parsing.vo.CommonMqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Miracle
 * @date 2023/2/10 11:31
 */
@Service
@RequiredArgsConstructor
public class StreamTaskServiceImpl implements StreamTaskService {

    private final StreamTaskMapper streamTaskMapper;

    private final RabbitMqSender rabbitMqSender;

    private final MqDefaultProperties mqDefaultProperties;

    private final DataBaseService dataBaseService;

    private static final String OUT_TIME = "OUT_TIME";

    @Override
    @Scheduled(fixedDelay = 60000)
    public void clearOutTimeTask() {
        LocalDateTime outTime = LocalDateTime.now().plusSeconds(-60);
        List<StreamTaskInfo> streamTaskInfoList = streamTaskMapper.selectByOutTimeTask(TaskState.RUNNING.getCode(), outTime);
        for (StreamTaskInfo streamTaskInfo : streamTaskInfoList){
            asynReqMap.remove(streamTaskInfo.getId());
            streamTaskInfo.setState(TaskState.ERROR.getCode());
            streamTaskInfo.setUpdateTime(LocalDateTime.now());
            streamTaskInfo.setDetail(OUT_TIME);
        }
        if (!streamTaskInfoList.isEmpty()){
            streamTaskMapper.updateAll(streamTaskInfoList);
        }
    }

    @Override
    public void sendMsgToGateway(Long dispatchId, String streamId, String msgType, Object data, DeferredResult<CommonResponse<?>> response) {
        DispatchInfo dispatchInfo = dataBaseService.getDispatchInfo(dispatchId);
        String mqId = UUID.randomUUID().toString().replace("-", "");
        Long taskId;
        if (Objects.isNull(response)){
            taskId = createTask(dispatchId, streamId, mqId, msgType, TaskState.SUCCESS);
        }else {
            taskId = createAsyncTask(dispatchId, streamId, mqId, msgType, response);
        }
        String mqKey = MqConstant.STREAM_PREFIX + MqConstant.GET_SET_PREFIX + dispatchId;
        CommonMqDto<Object> request = new CommonMqDto<>(dispatchInfo.getSerialNum(), msgType, taskId.toString(), LocalDateTime.now());
        request.setData(data);
        rabbitMqSender.sendMsgByRoutingKey(mqDefaultProperties.getStreamExchangeId(), mqKey, mqId, request, true);
    }

    @Override
    public Long createAsyncTask(Long dispatchId, String streamId, String mqId, String msgType, DeferredResult<CommonResponse<?>> deferredResult) {
        Long taskId = createTask(dispatchId, streamId, mqId, msgType, TaskState.RUNNING);
        asynReqMap.put(taskId, deferredResult);
        deferredResult.onTimeout(() -> {
            deferredResult.setResult(CommonResponse.failure(BusinessErrorEnums.FEIGN_REQUEST_TIME_OUT));
            asynReqMap.remove(taskId);
            taskError(taskId, BusinessErrorEnums.VALID_REQUEST_TIME_OUT, OUT_TIME);
        });
        return taskId;
    }


    @Override
    public Long createTask(Long dispatchId, String streamId, String mqId, String msgType, TaskState taskState) {
        LocalDateTime nowTime = LocalDateTime.now();
        StreamTaskInfo streamTaskInfo = new StreamTaskInfo();
        streamTaskInfo.setDispatchId(dispatchId);
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
        DeferredResult deferredResult = asynReqMap.remove(taskId);
        if (Objects.isNull(deferredResult)){
            streamTaskMapper.updateState(taskId, TaskState.ERROR.getCode(), String.format("任务成功返回，任务%s的返回请求丢失，消息内容：%s", taskId, data), LocalDateTime.now());
            return;
        }
        deferredResult.setResult(CommonResponse.success(data));
        streamTaskMapper.updateState(taskId, TaskState.SUCCESS.getCode(), null, LocalDateTime.now());
    }

    @Override
    public void taskError(Long taskId, BusinessErrorEnums errorEnums, String detail) {
        DeferredResult deferredResult = asynReqMap.remove(taskId);
        if (Objects.isNull(deferredResult)){
            streamTaskMapper.updateState(taskId, TaskState.ERROR.getCode(), String.format("任务失败返回，任务%s的返回请求丢失，异常信息%s, 消息内容：%s", taskId, errorEnums.getErrMsg(), detail), LocalDateTime.now());
            return;
        }
        deferredResult.setResult(CommonResponse.failure(errorEnums, detail));
        streamTaskMapper.updateState(taskId, TaskState.ERROR.getCode(), detail, LocalDateTime.now());
    }
}
