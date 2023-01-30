package com.runjian.parsing.protocol;


import com.runjian.parsing.constant.IdType;

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
     * 设备注册
     * @param data 数据集合
     */
    void deviceSignIn(Long gatewayId, Object data);

    /**
     * 规范化：设备同步
     * @param taskId 任务id
     * @param data 数据集合
     */
    void deviceSync(Long taskId, Object data);

    /**
     * 规范化：主动添加设备
     * @param taskId 任务id
     * @param data 数据集合
     */
    void deviceAdd(Long taskId, Object data);

    /**
     * 规范化：删除设备
     * @param taskId 任务id
     * @param data 数据集合
     */
    void deviceDelete(Long taskId, Object data);

    /**
     * 规范化：通道同步
     * @param taskId 任务id
     * @param data 数据集合
     */
    void channelSync(Long taskId, Object data);

    /**
     * 规范化：通道控制
     * @param taskId 任务id
     * @param data 数据集合
     */
    void channelPtzControl(Long taskId, Object data);

    /**
     * 规范化：通道播放
     * @param taskId 任务id
     * @param data 数据集合
     */
    void channelPlay(Long taskId, Object data);

    /**
     * 规范化：获取通道回放数据
     * @param taskId 任务id
     * @param data 数据集合
     */
    void channelRecord(Long taskId, Object data);

    /**
     * 规范化：通道回放
     * @param taskId 任务id
     * @param data 数据集合
     */
    void channelPlayback(Long taskId, Object data);

    /**
     * 通用消息处理
     * @param gatewayId
     * @param msgId
     * @param msgType
     * @param data
     */
    void commonEvent(Long gatewayId, String msgId, String msgType, Object data);

    /**
     * 通用消息处理
     * @param taskId 任务id
     * @param data 数据集合
     */
    void customEvent(Long taskId, Object data);
}
