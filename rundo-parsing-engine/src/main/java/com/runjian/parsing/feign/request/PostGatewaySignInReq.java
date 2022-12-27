package com.runjian.parsing.feign.request;

import com.runjian.parsing.entity.GatewayInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class PostGatewaySignInReq {

    public PostGatewaySignInReq(GatewayInfo gatewayInfo){
        BeanUtils.copyProperties(gatewayInfo, this);
    }

    /**
     * 主键id
     */
    private Long id;

    /**
     * 网关唯一序列号 网关ID
     */
    private String serialNum;

    /**
     * 网关名称
     */
    private String name;

    /**
     * 注册类型 1-MQ  2-RETFUL
     */
    private Integer signType;

    /**
     * 网关类型
     */
    private Integer gatewayType;

    /**
     * 协议
     */
    private String protocol;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;




}
