package com.runjian.parsing.service.common;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.TaskState;
import com.runjian.parsing.entity.GatewayTaskInfo;
import com.runjian.parsing.entity.StreamTaskInfo;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Miracle
 * @date 2023/2/10 11:15
 */
public interface StreamTaskService {

    /**
     * 异步返回体Map
     */
    Map<Long, DeferredResult<?>> asynReqMap = new ConcurrentHashMap<>(2000);

    /**
     * 清楚超时任务
     */
    void clearOutTimeTask();

    /**
     * 发送消息
     * @param dispatchId 网关id
     * @param streamId 流id
     * @param response 消息返回体
     * @param msgType 消息类型
     * @param data 数据
     */
    void sendMsgToGateway(Long dispatchId, String streamId, String msgType, Object data, DeferredResult<CommonResponse<?>> response);

    /**
     * 创建异步任务
     * @return 任务id
     */
    Long createAsyncTask(Long dispatchId, String streamId, String mqId, String msgType, DeferredResult<CommonResponse<?>> deferredResult);

    /**
     * 创建任务
     */
    Long createTask(Long dispatchId, String streamId, String mqId, String msgType, TaskState taskState);

    /**
     * 获取任务(带校验)
     * @param taskId 任务id
     * @return 任务信息
     */
    StreamTaskInfo getTaskValid(Long taskId, TaskState taskState);

    /**
     * 任务完成
     * @param taskId 任务id
     * @param data 数据
     * @param taskState 任务状态
     * @param errorEnums 错误枚举
     */
    void taskFinish(Long taskId, Object data, TaskState taskState, BusinessErrorEnums errorEnums);

}
