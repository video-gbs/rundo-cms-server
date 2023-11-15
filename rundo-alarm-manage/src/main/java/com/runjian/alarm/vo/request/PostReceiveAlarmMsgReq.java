package com.runjian.alarm.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 告警信息请求体
 * @author Miracle
 * @date 2023/9/19 14:41
 */
@Data
public class PostReceiveAlarmMsgReq  {

    /**
     * 通道Id
     */
    @NotNull(message = "通道id不能为空")
    @Range(min = 1, max = 9999999999L, message = "通道id不合法")
    private Long channelId;

    /**
     * 事件编码
     */
    @NotBlank(message = "事件编码不能为空")
    private String eventCode;

    /**
     * 事件类型
     */
    @NotNull(message = "事件消息类型不能为空")
    @Range(min = 1, max = 3, message = "事件类型范围在1~3")
    private Integer eventMsgType;

    /**
     * 事件描述
     */
    @Size(max = 500, message = "事件描述长度不能超过500")
    private String eventDesc;

    /**
     * 事件时间
     */
    @NotNull(message = "事件时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;

}
