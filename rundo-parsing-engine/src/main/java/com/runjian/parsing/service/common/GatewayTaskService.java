package com.runjian.parsing.service.common;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.entity.GatewayTaskInfo;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务服务
 * @author Miracle
 * @date 2023/1/16 10:42
 */
public interface GatewayTaskService {

    /**
     * 异步返回体Map
     */
    Map<Long, DeferredResult<?>> asynReqMap = new ConcurrentHashMap<>();

    /**
     * 发送消息
     * @param gatewayId 网关id
     * @param deviceId 设备id
     * @param channelId 通道id
     * @param response 消息返回体
     * @param msgType 消息类型
     * @param data 数据
     */
    void sendMsgToGateway(Long gatewayId, Long deviceId, Long channelId, String msgType, Object data, DeferredResult<CommonResponse<?>> response);

    /**
     * 创建异步任务
     * @return 任务id
     */
    Long createAsyncTask(Long gatewayId, Long deviceId, Long channelId, String mqId, String msgType, DeferredResult<CommonResponse<?>> deferredResult);

    /**
     * 创建任务
     */
    Long createTask(Long gatewayId, Long deviceId, Long channelId, String mqId, String msgType, TaskState taskState);

    /**
     * 获取任务
     * @param taskId 任务id
     * @return 任务信息
     */
    GatewayTaskInfo getTask(Long taskId);

    /**
     * 获取任务(带校验)
     * @param taskId 任务id
     * @return 任务信息
     */
    GatewayTaskInfo getTaskValid(Long taskId, TaskState taskState);

    /**
     * 获取并移除异步返回体
     * @param taskId 任务id
     * @return 异步返回体
     */
    DeferredResult<CommonResponse<?>> removeDeferredResult(Long taskId, TaskState taskState, String detail);


    /**
     * 任务完成
     * @param taskId 任务id
     */
    void taskSuccess(Long taskId, Object data);

    /**
     * 任务异常
     * @param taskId 任务id
     * @param detail 说明
     */
    void taskError(Long taskId, BusinessErrorEnums errorEnums, String detail);


}
