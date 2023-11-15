package com.runjian.alarm.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/9/11 11:47
 */
@Data
public class GetAlarmSchemeEventNameRsp {

    /**
     * 预案id
     */
    private Long schemeId;

    /**
     * 事件名称
     */
    private String eventName;
}
