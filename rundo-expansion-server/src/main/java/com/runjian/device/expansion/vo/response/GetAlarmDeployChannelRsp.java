package com.runjian.device.expansion.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/9/11 11:08
 */
@Data
public class GetAlarmDeployChannelRsp {

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 预案id
     */
    private Long schemeId;

    /**
     * 布防状态 0:撤防 1:布防
     */
    private Integer deployState;
}
