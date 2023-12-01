package com.runjian.parsing.service.common;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
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
    Map<Long, DeferredResult<?>> asynReqMap = new ConcurrentHashMap<>(2000);

    /**
     * 定时清理超时任务
     */
    void clearOutTimeTask();

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
     * 获取任务(带校验)
     * @param taskId 任务id
     * @return 任务信息
     */
    GatewayTaskInfo getTaskValid(Long taskId);

    /**
     * 任务完成
     * @param taskId 任务消息体
     * @param data 数据
     * @param taskState 任务状态
     * @param errorEnums 错误枚举
     */
    void taskFinish(Long taskId, Object data, TaskState taskState, BusinessErrorEnums errorEnums);

}
