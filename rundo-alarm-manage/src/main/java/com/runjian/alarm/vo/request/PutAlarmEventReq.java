package com.runjian.alarm.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 修改事件请求体
 * @author Miracle
 * @date 2023/9/19 15:30
 */
@Data
public class PutAlarmEventReq {

    /**
     * 事件id
     */
    @NotNull(message = "事件id不能为空")
    @Range(min = 1, max = 9999999999L, message = "事件id不合法")
    private Long id;

    /**
     * 事件名称
     */
    @NotBlank(message = "事件名称不能为空")
    @Size(max = 50, message = "事件名称长度不能超过50")
    private String eventName;

    /**
     * 事件排序
     */
    @NotNull(message = "事件排序不能为空")
    @Range(min = 0, max = 99999999, message = "事件排序不合法")
    private Integer eventSort;

    /**
     * 事件描述
     */
    @Size(max = 250, message = "事件描述长度不能超过250")
    private String eventDesc;
}
