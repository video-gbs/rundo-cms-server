package com.runjian.parsing.service.south;

import com.runjian.parsing.vo.response.SignInRsp;

/**
 * @author Miracle
 * @date 2023/2/7 17:59
 */
public interface DispatchService {

    /**
     * 调度服务注册
     * @param serialNum 序列号
     * @param signType 注册类型
     * @param ip ip地址
     * @param port 端口
     * @param outTime 心跳过期时间
     */
    SignInRsp signIn(String serialNum, Integer signType, String ip, String port, String outTime);


    /**
     * 调度服务心跳
     * @param serialNum 网关序列号
     * @param heartbeatTime 心跳过期时间
     * @return 网关id
     */
    Long heartbeat(String serialNum, String heartbeatTime);
}
