package com.runjian.alarm.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/9/11 14:41
 */
@Data
public class GetAlarmEventNameRsp {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 事件排序
     */
    private Integer eventSort;
}
