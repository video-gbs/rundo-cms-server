package com.runjian.parsing.service.protocol;


import com.runjian.parsing.vo.CommonMqDto;

/**
 * @author Miracle
 * @date 2023/1/28 14:59
 */
public interface SouthProtocol {

    String DEFAULT_PROTOCOL = "DEFAULT";

    /**
     * 规范化：获取默认的协议处理器,这个方法必须复写
     * @return
     */
    String getProtocolName();

    /**
     * 消息分发
     * @param msgType
     * @param taskId
     * @param dataMap
     */
    void msgDistribute(String msgType, Long gatewayId, Long taskId, Object dataMap);

    /**
     * 通用消息处理
     * @param taskId 任务id
     * @param data 数据集合
     */
    void customEvent(Long taskId, Object data);

    /**
     * 异常处理
     */
    void errorEvent(Long taskId, CommonMqDto<?> response);
}
