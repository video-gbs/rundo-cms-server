package com.runjian.alarm.vo.response;

import lombok.Data;

/**
 * 告警事件返回体
 * @author Miracle
 * @date 2023/9/11 9:28
 */
@Data
public class GetAlarmEventRsp {

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

    /**
     * 事件描述
     */
    private String eventDesc;

}
