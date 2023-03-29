package com.runjian.parsing.service.south;

import com.runjian.parsing.vo.CommonMqDto;

/**
 * @author Miracle
 * @date 2023/2/9 15:14
 */
public interface StreamSouthService {

    /**
     * 消息分发
     * @param msgType
     * @param dispatchId
     * @param data
     */
    void msgDistribute(String msgType, Long dispatchId, Long taskId, Object data);

    /**
     * 通用消息返回
     * @param taskId
     * @param data
     */
    void taskEvent(Long taskId, Object data);

    /**
     * 错误消息返回
     * @param taskId
     * @param response
     */
    void errorEvent(Long taskId, CommonMqDto<?> response);
}
