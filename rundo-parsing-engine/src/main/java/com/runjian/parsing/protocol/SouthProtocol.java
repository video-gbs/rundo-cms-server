package com.runjian.parsing.protocol;

import java.util.Map;

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
     * @param dataMap 数据集合
     */
    void deviceSignIn(Object dataMap);

    /**
     * 规范化：设备同步
     * @param taskId 任务id
     * @param dataMap 数据集合
     */
    void deviceSync(Long taskId, Object dataMap);

    /**
     * 规范化：主动添加设备
     * @param taskId 任务id
     * @param dataMap 数据集合
     */
    void deviceAdd(Long taskId, Object dataMap);

    /**
     * 规范化：删除设备
     * @param taskId 任务id
     * @param dataMap 数据集合
     */
    void deviceDelete(Long taskId, Object dataMap);

    /**
     * 规范化：通道同步
     * @param taskId 任务id
     * @param dataMap 数据集合
     */
    void channelSync(Long taskId, Object dataMap);

    /**
     * 规范化：通道播放
     * @param taskId 任务id
     * @param dataMap 数据集合
     */
    void channelPlay(Long taskId, Object dataMap);

    /**
     * 规范化：获取通道回放数据
     * @param taskId 任务id
     * @param dataMap 数据集合
     */
    void getChannelRecord(Long taskId, Object dataMap);

    /**
     * 规范化：通道回放
     * @param taskId 任务id
     * @param dataMap 数据集合
     */
    void channelPlayback(Long taskId, Object dataMap);

    /**
     * 通用消息处理
     * @param taskId 任务id
     * @param dataMap 数据集合
     */
    void customEvent(Long taskId, Object dataMap);
}