package com.runjian.parsing.vo.request;

import com.runjian.parsing.entity.GatewayInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * 网关注册请求体
 * @author Miracle
 * @date 2023/1/13 11:44
 */
@Data
public class PostGatewaySignInReq {

    public PostGatewaySignInReq(GatewayInfo gatewayInfo, LocalDateTime outTime){
        BeanUtils.copyProperties(gatewayInfo, this);
        this.outTime = outTime;
        this.gatewayId = gatewayInfo.getId();
    }

    /**
     * 主键id
     */
    private Long gatewayId;

    /**
     * 网关唯一序列号 网关ID
     */
    private String serialNum;

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

    /**
     * 过期时间
     */
    private LocalDateTime outTime;

}
