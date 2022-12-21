package com.runjian.parsing.vo.request;

import com.runjian.parsing.entity.GatewayInfo;
import lombok.Data;

@Data
public class GatewaySignInReq {


    /**
     * 协议
     */
    private String protocol;

    /**
     * 网关类型
     */
    private String gatewayType;

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

}
