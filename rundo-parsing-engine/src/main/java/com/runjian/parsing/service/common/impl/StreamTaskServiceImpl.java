package com.runjian.parsing.service.common.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.common.constant.MsgType;
import com.runjian.parsing.constant.MqConstant;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.dao.StreamTaskMapper;
import com.runjian.parsing.entity.DispatchInfo;
import com.runjian.parsing.entity.GatewayTaskInfo;
import com.runjian.parsing.entity.StreamTaskInfo;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.mq.listener.MqDefaultProperties;
import com.runjian.parsing.service.common.CommonTaskService;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.service.common.StreamTaskService;
import com.runjian.parsing.utils.RedisLockUtil;
import com.runjian.parsing.vo.CommonMqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Miracle
 * @date 2023/2/10 11:31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StreamTaskServiceImpl implements StreamTaskService {

    private final StreamTaskMapper streamTaskMapper;

    private final RabbitMqSender rabbitMqSender;

    private final MqDefaultProperties mqDefaultProperties;

    private final DataBaseService dataBaseService;

    private final RedissonClient redissonClient;

    private final RedisLockUtil redisLockUtil;

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
        MsgType msgTypeEnum = MsgType.getByStr(msgType);
        Long taskId;
        if (Objects.isNull(response)){
            taskId = createTask(dispatchId, streamId, mqId, msgType, TaskState.SUCCESS);
        }else {
            taskId = createAsyncTask(dispatchId, streamId, mqId, msgType, response);
        }
        if (msgTypeEnum.getIsMerge()){
            redissonClient.getQueue(MarkConstant.REDIS_MQ_STREAM_MERGE_LIST + streamId).offer(taskId);
            if (redisLockUtil.lock(MarkConstant.REDIS_STREAM_REQUEST_MERGE_LOCK + streamId, taskId.toString(), 15, TimeUnit.SECONDS, 0)){
                sendMsg(dispatchId, msgType, data, dispatchInfo, taskId, mqId);
            }
        } else {
            sendMsg(dispatchId, msgType, data, dispatchInfo, taskId, mqId);
        }

    }

    private void sendMsg(Long dispatchId, String msgType, Object data, DispatchInfo dispatchInfo, Long taskId, String mqId) {
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
            taskFinish(taskId, OUT_TIME, TaskState.ERROR, BusinessErrorEnums.VALID_REQUEST_TIME_OUT);
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
    public void taskFinish(Long taskId, Object data, TaskState taskState, BusinessErrorEnums errorEnums) {
        StreamTaskInfo streamTaskInfo = getTaskValid(taskId, taskState);
        MsgType msgType = MsgType.getByStr(streamTaskInfo.getMsgType());
        if (msgType.getIsMerge()){
            List<Long> taskIdList = CommonTaskService.getAllTask(redissonClient.getQueue(MarkConstant.REDIS_GATEWAY_REQUEST_MERGE_LOCK + streamTaskInfo.getStreamId())) ;
            if (!taskIdList.isEmpty()){
                List<Long> finishTaskIdList = new ArrayList<>(taskIdList.size());
                for (Long taskIdOb : taskIdList){
                    DeferredResult deferredResult = asynReqMap.remove(streamTaskInfo.getId());
                    finishTaskIdList.add(taskIdOb);
                    if (Objects.isNull(deferredResult)){
                        data = String.format("返回请求丢失，消息内容：%s", data);
                    }else {
                        CommonTaskService.taskSetResult(data, taskState, errorEnums, deferredResult);
                    }
                }
                streamTaskMapper.batchUpdateState(finishTaskIdList, taskState.getCode(), data.toString(), LocalDateTime.now());
            }
            redisLockUtil.unLock(MarkConstant.REDIS_STREAM_REQUEST_MERGE_LOCK + streamTaskInfo.getStreamId(), taskId.toString());
        }else {
            DeferredResult deferredResult = asynReqMap.remove(streamTaskInfo.getId());
            if (Objects.isNull(deferredResult)){
                streamTaskMapper.updateState(streamTaskInfo.getId(), TaskState.ERROR.getCode(), String.format("返回请求丢失，消息内容：%s", data), LocalDateTime.now());
            }else {
                CommonTaskService.taskSetResult(data, taskState, errorEnums, deferredResult);
                streamTaskMapper.updateState(streamTaskInfo.getId(), taskState.getCode(), data.toString(), LocalDateTime.now());
            }
        }
    }

}
