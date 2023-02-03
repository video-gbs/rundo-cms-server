package com.runjian.stream.service;

import java.time.LocalDateTime;

/**
 * 调度服务
 * @author Miracle
 * @date 2023/2/3 10:27
 */
public interface DispatchService {

    /**
     * 调度服务心跳处理
     */
    void heartbeat();

    /**
     * 调度服务注册
     * @param dispatchId 调度服务id
     * @param serialNum 调度服务序列号
     * @param ip ip地址
     * @param port 端口
     * @param outTime 超时时间s
     */
    void signIn(Long dispatchId, String serialNum, String ip, String port, LocalDateTime outTime);

    /**
     * 修改额外的名字与可访问URL
     * @param dispatchId 分配id
     * @param name 名字
     * @param url 可访问url
     */
    void updateExtraData(Long dispatchId, String name, String url);

    /**
     * 更新心跳信息
     * @param dispatchId 网关ID
     * @param outTime 过期时间
     */
    void updateHeartbeat(Long dispatchId, LocalDateTime outTime);
}
