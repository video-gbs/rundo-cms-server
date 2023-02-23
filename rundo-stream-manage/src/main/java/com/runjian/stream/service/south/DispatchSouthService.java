package com.runjian.stream.service.south;

import java.time.LocalDateTime;

/**
 * 调度服务
 * @author Miracle
 * @date 2023/2/3 10:27
 */
public interface DispatchSouthService {

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
     * 更新心跳信息
     * @param dispatchId 网关ID
     * @param outTime 过期时间
     */
    Boolean updateHeartbeat(Long dispatchId, LocalDateTime outTime);
}
