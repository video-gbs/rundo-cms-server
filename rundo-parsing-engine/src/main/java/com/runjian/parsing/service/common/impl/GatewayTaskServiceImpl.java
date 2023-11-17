package com.runjian.parsing.service.common.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.MqConstant;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.dao.GatewayTaskMapper;
import com.runjian.parsing.entity.GatewayInfo;
import com.runjian.parsing.entity.GatewayTaskInfo;
import com.runjian.parsing.mq.config.RabbitMqSender;
import com.runjian.parsing.mq.listener.MqDefaultProperties;
import com.runjian.parsing.service.common.DataBaseService;
import com.runjian.parsing.service.common.GatewayTaskService;
import com.runjian.parsing.vo.CommonMqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
    public void sendMsgToGateway(Long gatewayId, Long deviceId, Long channelId, String msgType, Object data, DeferredResult<CommonResponse<?>> response) {
        GatewayInfo gatewayInfo = dataBaseService.getGatewayInfo(gatewayId);
        String mqId = UUID.randomUUID().toString().replace("-", "");
        Long taskId;
        if (Objects.isNull(response)){
            taskId = createTask(gatewayId, deviceId, channelId, mqId, msgType, TaskState.SUCCESS);
        }else {
            taskId = createAsyncTask(gatewayId, deviceId, channelId, mqId, msgType, response);
        }
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
            taskError(taskId, BusinessErrorEnums.VALID_REQUEST_TIME_OUT, OUT_TIME);
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

        gatewayTaskInfo.setState(taskState.getCode());
        gatewayTaskMapper.save(gatewayTaskInfo);
        return gatewayTaskInfo.getId();
    }

    @Override
    public GatewayTaskInfo getTask(Long taskId) {
        Optional<GatewayTaskInfo> taskInfoOp = gatewayTaskMapper.selectById(taskId);
        return taskInfoOp.orElse(null);
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
    public DeferredResult removeDeferredResult(Long taskId, TaskState taskState, String detail) {
        gatewayTaskMapper.updateState(taskId, taskState.getCode(), detail, LocalDateTime.now());
        return asynReqMap.remove(taskId);

    }


    @Override
    public void taskSuccess(Long taskId, Object data) {
        DeferredResult deferredResult = asynReqMap.remove(taskId);
        if (Objects.isNull(deferredResult)){
            gatewayTaskMapper.updateState(taskId, TaskState.ERROR.getCode(), String.format("任务%s的返回请求丢失，消息内容：%s", taskId, data), LocalDateTime.now());
            return;
        }
        deferredResult.setResult(CommonResponse.success(data));
        gatewayTaskMapper.updateState(taskId, TaskState.SUCCESS.getCode(), null, LocalDateTime.now());
    }

    @Override
    public void taskError(Long taskId, BusinessErrorEnums errorEnums, String detail) {
        DeferredResult deferredResult = asynReqMap.remove(taskId);
        if (Objects.isNull(deferredResult)){
            gatewayTaskMapper.updateState(taskId, TaskState.ERROR.getCode(), String.format("任务失败返回，任务%s的返回请求丢失，异常信息%s, 消息内容：%s", taskId, errorEnums.getErrMsg(), detail), LocalDateTime.now());
            return;
        }
        deferredResult.setResult(CommonResponse.failure(errorEnums, detail));
        gatewayTaskMapper.updateState(taskId, TaskState.ERROR.getCode(), detail, LocalDateTime.now());
    }


}
