package com.runjian.parsing.service.common;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.TaskState;
import org.redisson.api.RQueue;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/11/24 14:49
 */
public class CommonTaskService {

    /**
     * 获取全部任务
     * @param key
     * @return
     */
    public static List<Long> getAllTask(RQueue<Long> rQueue){
        // 原子性读取并删除全部List
        List<Long> taskIdList = new ArrayList<>();
        while (true) {
            Long taskId = rQueue.poll();
            if (Objects.isNull(taskId)){
                break;
            }
            taskIdList.add(taskId);
        }
        return taskIdList;
    }

    /**
     * 获取主要id
     * @param gatewayId
     * @param deviceId
     * @param channelId
     * @return
     */
    public static Long getMainId(Long gatewayId, Long deviceId, Long channelId) {
        Long id = null;
        if (Objects.nonNull(channelId)){
            id = channelId;
        } else if (Objects.nonNull(deviceId)) {
            id = deviceId;
        } else if (Objects.nonNull(gatewayId)) {
            id = gatewayId;
        }
        return id;
    }

    /**
     * 设置返回结果
     * @param data
     * @param taskState
     * @param errorEnums
     * @param deferredResult
     */
    public static void taskSetResult(Object data, TaskState taskState, BusinessErrorEnums errorEnums, DeferredResult deferredResult) {
        if (TaskState.ERROR.equals(taskState)){
            if (data instanceof CommonResponse){
                deferredResult.setResult(data);
            }else {
                deferredResult.setResult(CommonResponse.failure(errorEnums, data));
            }
        }else {
            deferredResult.setResult(CommonResponse.success(data));
        }
    }
}
