package com.runjian.parsing.vo;

import com.runjian.common.config.response.CommonResponse;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    public  static <T> GatewayMqDto<T> createByCommonResponse(CommonResponse<T> commonResponse){
        GatewayMqDto<T> gatewayMqDto = new GatewayMqDto<>();
        gatewayMqDto.setCode(commonResponse.getCode());
        gatewayMqDto.setMsg(commonResponse.getMsg());
        gatewayMqDto.setData(commonResponse.getData());
        return gatewayMqDto;
    }

}

