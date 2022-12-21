package com.runjian.parsing.service;

import com.runjian.parsing.vo.response.GatewaySignInRsp;

public interface GatewayService {

    /**
     * 网关注册
     */
    GatewaySignInRsp signIn(String serialNum, Integer signType, Integer gatewayType, String protocol, String ip, String port);

}
