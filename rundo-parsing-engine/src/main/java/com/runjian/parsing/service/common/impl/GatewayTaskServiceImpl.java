package com.runjian.parsing.service.common.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.MarkConstant;
import com.runjian.common.constant.MsgType;
import com.runjian.parsing.constant.MqConstant;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.dao.GatewayTaskMapper;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.entity.GatewayTaskInfo;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.mq.listener.MqDefaultProperties;
import com.runjian.parsing.service.common.CommonTaskService;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.service.common.GatewayTaskService;
import com.runjian.parsing.utils.RedisLockUtil;
import com.runjian.parsing.vo.CommonMqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 任务服务
 * @author Miracle
 * @date 2023/1/16 11:37
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayTaskServiceImpl implements GatewayTaskService {

    private final GatewayTaskMapper gatewayTaskMapper;

    private final RabbitMqSender rabbitMqSender;

    private final DataBaseService dataBaseService;

    private final MqDefaultProperties mqDefaultProperties;

    private final RedissonClient redissonClient;

    private final RedisLockUtil redisLockUtil;


    private static final String OUT_TIME = "OUT_TIME";


    @Override
    @Scheduled(fixedDelay = 60000)
    public void clearOutTimeTask() {
        LocalDateTime outTime = LocalDateTime.now().plusSeconds(-60);
        List<GatewayTaskInfo> gatewayTaskInfoList = gatewayTaskMapper.selectByOutTimeTask(TaskState.RUNNING.getCode(), outTime);
        for (GatewayTaskInfo gatewayTaskInfo : gatewayTaskInfoList){
            asynReqMap.remove(gatewayTaskInfo.getId());
            gatewayTaskInfo.setState(TaskState.ERROR.getCode());
            gatewayTaskInfo.setUpdateTime(LocalDateTime.now());
            gatewayTaskInfo.setDetail(OUT_TIME);
        }
        if (!gatewayTaskInfoList.isEmpty()){
            gatewayTaskMapper.updateAll(gatewayTaskInfoList);
        }
    }

    /**
     * 发送消息
     * @param gatewayId 网关id
     * @param deviceId 设备id
     * @param channelId 通道id
     * @param response 消息返回体
     * @param msgType 消息类型
     * @param data 数据
     */
    @Override
    public void sendMsgToGateway(Long gatewayId, Long deviceId, Long channelId, String msgType, Object data, DeferredResult<CommonResponse<?>> response) {
        GatewayInfo gatewayInfo = dataBaseService.getGatewayInfo(gatewayId);
        String mqId = UUID.randomUUID().toString().replace("-", "");
        MsgType msgTypeEnum = MsgType.getByStr(msgType);
        Long taskId;
        if (Objects.isNull(response)){
            taskId = createTask(gatewayId, deviceId, channelId, mqId, msgType, TaskState.SUCCESS);
        }else {
            taskId = createAsyncTask(gatewayId, deviceId, channelId, mqId, msgType, response);
        }
        if (msgTypeEnum.getIsMerge()){
            Long mainId = CommonTaskService.getMainId(gatewayId, deviceId, channelId);
            RBucket<Long> bucket = redissonClient.getBucket(MarkConstant.REDIS_GATEWAY_REQUEST_MERGE_LOCK + MarkConstant.MARK_SPLIT_SEMICOLON + msgType.toUpperCase() + MarkConstant.MARK_SPLIT_SEMICOLON + mainId);
            Long oldTaskId = bucket.get();
            if (bucket.trySet(taskId, 0 ,TimeUnit.SECONDS)){
                log.warn("任务 {} 创建任务队列", taskId);
                RQueue<Long> rqueue = redissonClient.getQueue(MarkConstant.REDIS_GATEWAY_REQUEST_MERGE_LIST + taskId);
                rqueue.offer(taskId);
                bucket.expire(10,  TimeUnit.SECONDS);
                rqueue.expire(15, TimeUnit.SECONDS);
                sendMsg(gatewayId, msgType, data, gatewayInfo, taskId, mqId);
            } else {
                log.warn("任务 {} 进入任务队列 {} ", taskId, oldTaskId);
                redissonClient.getQueue(MarkConstant.REDIS_GATEWAY_REQUEST_MERGE_LIST + oldTaskId).offer(taskId);
            }
        } else {
            sendMsg(gatewayId, msgType, data, gatewayInfo, taskId, mqId);
        }
    }

    private void sendMsg(Long gatewayId, String msgType, Object data, GatewayInfo gatewayInfo, Long taskId, String mqId) {
        String mqKey = MqConstant.GATEWAY_PREFIX + MqConstant.GET_SET_PREFIX + gatewayId;
        CommonMqDto<Object> request = new CommonMqDto<>(gatewayInfo.getSerialNum(), msgType, taskId.toString(), LocalDateTime.now());
        request.setData(data);
        rabbitMqSender.sendMsgByRoutingKey(mqDefaultProperties.getGatewayExchangeId(), mqKey, mqId, request, true);
    }


    @Override
    public Long createAsyncTask(Long gatewayId, Long deviceId, Long channelId, String mqId, String msgType, DeferredResult<CommonResponse<?>> deferredResult) {
        Long taskId = createTask(gatewayId, deviceId, channelId, mqId, msgType, TaskState.RUNNING);
        asynReqMap.put(taskId, deferredResult);
        deferredResult.onTimeout(() -> {
            deferredResult.setResult(CommonResponse.failure(BusinessErrorEnums.FEIGN_REQUEST_TIME_OUT));
            asynReqMap.remove(taskId);
            taskFinish(taskId, OUT_TIME, TaskState.ERROR, BusinessErrorEnums.VALID_REQUEST_TIME_OUT);
        });
        return taskId;
    }

    @Override
    public Long createTask(Long gatewayId, Long deviceId, Long channelId, String mqId, String msgType, TaskState taskState) {
        LocalDateTime nowTime = LocalDateTime.now();
        GatewayTaskInfo gatewayTaskInfo = new GatewayTaskInfo();
        gatewayTaskInfo.setGatewayId(gatewayId);
        gatewayTaskInfo.setDeviceId(deviceId);
        gatewayTaskInfo.setChannelId(channelId);
        gatewayTaskInfo.setMqId(mqId);
        gatewayTaskInfo.setMsgType(msgType);
        gatewayTaskInfo.setCreateTime(nowTime);
        gatewayTaskInfo.setUpdateTime(nowTime);
        gatewayTaskInfo.setState(taskState.getCode());
        gatewayTaskMapper.save(gatewayTaskInfo);
        return gatewayTaskInfo.getId();
    }

    @Override
    public GatewayTaskInfo getTaskValid(Long taskId, TaskState taskState) {
        Optional<GatewayTaskInfo> taskInfoOp = gatewayTaskMapper.selectById(taskId);
        if (taskInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("任务%s不存在", taskId));
        }
        GatewayTaskInfo gatewayTaskInfo = taskInfoOp.get();
        if (!gatewayTaskInfo.getState().equals(taskState.getCode())){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, String.format("任务状态异常，当前任务状态：%s", TaskState.getMsg(gatewayTaskInfo.getState())));
        }
        return gatewayTaskInfo;
    }

    @Override
    public void taskFinish(Long taskId, Object data, TaskState taskState, BusinessErrorEnums errorEnums)  {
        GatewayTaskInfo gatewayTaskInfo = getTaskValid(taskId, taskState);
        MsgType msgType = MsgType.getByStr(gatewayTaskInfo.getMsgType());
        if (msgType.getIsMerge()){
            RQueue<Long> rqueue = redissonClient.getQueue(MarkConstant.REDIS_GATEWAY_REQUEST_MERGE_LIST + taskId);
            boolean isFirstRun = true;
            while (rqueue.isExists()){
                List<Long> taskIdList = CommonTaskService.getAllTaskExceptTask(rqueue, taskId) ;
                if (isFirstRun){
                    taskIdList.add(taskId);
                    isFirstRun = false;
                }
                log.warn("当前任务Id:{} 即将处理的任务id:{}", taskId, taskIdList);
                if (!taskIdList.isEmpty()){
                    List<Long> finishTaskIdList = new ArrayList<>(taskIdList.size());
                    for (Long taskIdOb : taskIdList){
                        DeferredResult deferredResult = asynReqMap.remove(gatewayTaskInfo.getId());
                        finishTaskIdList.add(taskIdOb);
                        if (Objects.isNull(deferredResult)){
                            data = String.format("返回请求丢失，消息内容：%s", data);
                        }else {
                            CommonTaskService.taskSetResult(data, taskState, errorEnums, deferredResult);
                        }
                    }
                    gatewayTaskMapper.batchUpdateState(finishTaskIdList, taskState.getCode(), Objects.isNull(data) ? null : data.toString(), LocalDateTime.now());
                }else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new BusinessException(BusinessErrorEnums.UNKNOWN_ERROR, "网关消息聚合线程恢复异常：" + e.getMessage());
                    }
                }
            }
            log.warn("当前任务Id:{} 任务结束", taskId);

        }else {
            DeferredResult deferredResult = asynReqMap.remove(gatewayTaskInfo.getId());
            if (Objects.isNull(deferredResult)){
                gatewayTaskMapper.updateState(gatewayTaskInfo.getId(), TaskState.ERROR.getCode(), String.format("返回请求丢失，消息内容：%s", data), LocalDateTime.now());
            } else {
                CommonTaskService.taskSetResult(data, taskState, errorEnums, deferredResult);
                gatewayTaskMapper.updateState(gatewayTaskInfo.getId(), taskState.getCode(), Objects.isNull(data) ? null : data.toString(), LocalDateTime.now());
            }
        }
    }
}
