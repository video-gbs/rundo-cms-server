package com.runjian.alarm.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 添加事件请求体
 * @author Miracle
 * @date 2023/9/19 15:30
 */
@Data
public class PostAlarmEventReq {

    /**
     * 事件名称
     */
    @NotBlank(message = "事件名称不能为空")
    @Size(max = 50, message = "事件名称长度不能超过50")
    private String eventName;

    /**
     * 事件编码
     */
    @NotBlank(message = "事件编码不能为空")
    @Size(max = 100, message = "事件编码长度不能超过100")
    private String eventCode;

    /**
     * 事件排序
     */
    @NotNull(message = "事件排序不能为空")
    @Range(min = 0, max = 99999999, message = "事件排序不合法")
    private Integer eventSort;

    /**
     * 事件描述
     */
    @NotBlank(message = "事件描述不能为空")
    @Size(max = 250, message = "事件描述长度不能超过250")
    private String eventDesc;
}
