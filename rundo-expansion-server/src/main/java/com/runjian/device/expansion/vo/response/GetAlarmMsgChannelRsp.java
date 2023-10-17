package com.runjian.device.expansion.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/9/11 16:26
 */
@Data
public class GetAlarmMsgChannelRsp {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 通道主键Id
     */
    private Long channelId;

    /**
     * 告警等级
     */
    private String alarmLevel;

    /**
     * 告警类型
     */
    private String alarmType;

    /**
     * 告警时间
     */
    private LocalDateTime alarmTime;

    /**
     * 告警描述
     */
    private String alarmDesc;

    /**
     * 播放地址
     */
    private String videoUrl;

    /**
     * 视频状态：0->失败重试 1->进行中 2->已完成 3->异常
     */
    private Integer videoState;

    /**
     * 错误描述
     */
    private String videoErrorMsg;
}
