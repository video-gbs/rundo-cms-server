package com.runjian.parsing.service;

import com.runjian.parsing.vo.response.GatewayHeartbeatRsp;
import com.runjian.parsing.vo.response.GatewaySignInRsp;

import java.time.LocalDateTime;

public interface GatewayService {

    /**
     * 网关注册
     */
    GatewaySignInRsp signIn(String serialNum, Integer signType, Integer gatewayType, String protocol, String ip, String port, String outTime);

    /**
     * 网关心跳
     */
    Long heartbeat(String serialNum, LocalDateTime heartbeatTime);

}
