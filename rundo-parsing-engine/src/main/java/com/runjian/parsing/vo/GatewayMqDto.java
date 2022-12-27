package com.runjian.parsing.vo;

import com.runjian.common.config.response.CommonResponse;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 网关传输消息体
 */
@Data
public class GatewayMqDto<T> extends CommonResponse<T> {


    /**
     * 网关序列号
     */
    private String serialNum;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * time
     */
    private LocalDateTime time;

    /**
     * 复制请求体信息做返回
     * @param gatewayMqDto
     */
    public void copyRequest(GatewayMqDto gatewayMqDto){
        this.serialNum = gatewayMqDto.getSerialNum();
        this.msgType = gatewayMqDto.getMsgType();
        this.msgId = gatewayMqDto.getMsgId();
        this.time = LocalDateTime.now();
    }

}

