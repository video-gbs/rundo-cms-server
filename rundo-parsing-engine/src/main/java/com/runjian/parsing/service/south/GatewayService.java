package com.runjian.parsing.service.south;

import com.runjian.parsing.vo.response.SignInRsp;

import java.util.Set;

public interface GatewayService {

    /**
     * 网关注册
     * @param serialNum 序列号
     * @param signType 注册类型
     * @param gatewayType 网关类型
     * @param protocol 协议
     * @param ip ip地址
     * @param port 端口
     * @param outTime 心跳过期时间
     */
    SignInRsp signIn(String serialNum, Integer signType, Integer gatewayType, String protocol, String ip, String port, String outTime);


     /**
     * 网关心跳
     * @param serialNum 网关序列号
     * @param heartbeatTime 心跳过期时间
     * @return 网关id
     */
    Long heartbeat(String serialNum, String heartbeatTime);

    /**
     * 网关全量同步
     * @param gatewayIds 网关id数组
     */
    void totalSync(Set<Long> gatewayIds);
}
