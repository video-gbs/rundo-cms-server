package com.runjian.alarm.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/9/11 11:08
 */
@Data
public class GetAlarmChannelRsp {

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 预案id
     */
    private Long schemeId;

    /**
     * 预案名称
     */
    private String schemeName;

}
