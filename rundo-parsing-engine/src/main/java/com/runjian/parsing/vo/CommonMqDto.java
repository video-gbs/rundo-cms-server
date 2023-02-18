package com.runjian.parsing.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.runjian.common.config.response.CommonResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 网关传输消息体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonMqDto<T> extends CommonResponse<T> {


    /**
     * 网关序列号
     */
    private String serialNum;

    /**
     * 消息类型 {@link com.runjian.parsing.constant.MsgType}
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime time;

    /**
     * 复制请求体信息做返回
     * @param commonMqDto
     */
    public void copyRequest(CommonMqDto commonMqDto){
        this.serialNum = commonMqDto.getSerialNum();
        this.msgType = commonMqDto.getMsgType();
        this.msgId = commonMqDto.getMsgId();
        this.time = LocalDateTime.now();
    }

    public  static <T> CommonMqDto<T> createByCommonResponse(CommonResponse<T> commonResponse){
        CommonMqDto<T> commonMqDto = new CommonMqDto<>();
        commonMqDto.setCode(commonResponse.getCode());
        commonMqDto.setMsg(commonResponse.getMsg());
        commonMqDto.setData(commonResponse.getData());
        return commonMqDto;
    }

}

