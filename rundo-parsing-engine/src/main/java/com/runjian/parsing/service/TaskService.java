package com.runjian.parsing.service;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.MsgType;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.entity.TaskInfo;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务服务
 * @author Miracle
 * @date 2023/1/16 10:42
 */
public interface TaskService {

    /**
     * 异步返回体Map
     */
    Map<Long, DeferredResult<?>> asynReqMap = new ConcurrentHashMap<>();

    /**
     * 创建异步任务
     * @return 任务id
     */
    Long createAsyncTask(Long gatewayId, Long deviceId, Long channelId, String clientMsgId, String mqId, String msgType, DeferredResult<CommonResponse<?>> deferredResult);

    /**
     * 创建任务
     */
    Long createTask(Long gatewayId, Long deviceId, Long channelId, String clientMsgId, String mqId, String msgType, TaskState taskState, String desc);

    /**
     * 获取任务
     * @param taskId 任务id
     * @return 任务信息
     */
    TaskInfo getTask(Long taskId);

    /**
     * 获取任务(带校验)
     * @param taskId 任务id
     * @return 任务信息
     */
    TaskInfo getTaskValid(Long taskId, TaskState taskState);

    /**
     * 获取并移除异步返回体
     * @param taskId 任务id
     * @return 异步返回体
     */
    DeferredResult<CommonResponse<?>> removeDeferredResult(Long taskId);

    /**
     * 修改任务信息
     * @param id
     * @param deviceId
     * @param channelId
     * @param msgType
     * @param desc
     */
    void updateTaskInfo(Long id, Long deviceId, Long channelId, MsgType msgType, String mqId, String desc);

    /**
     * 任务完成
     * @param taskId 任务id
     */
    void taskSuccess(Long taskId);

    /**
     * 任务异常
     * @param taskId 任务id
     * @param detail 说明
     */
    void taskError(Long taskId, String detail);


}
