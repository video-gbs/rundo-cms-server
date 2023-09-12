package com.runjian.alarm.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 告警信息
 * @author Miracle
 * @date 2023/9/8 10:20
 */
@Data
public class AlarmMsgInfo {

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
    private Integer alarmLevel;

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
     * 播放时长
     */
    private Integer videoLength;

    /**
     * 音频状态
     */
    private Integer videoAudioState;

    /**
     * 视频流ID
     */
    private String videoStreamId;

    /**
     * 视频状态：0->失败重试 1->进行中 2->已完成 3->异常
     */
    private Integer photoState;

    /**
     * 图片地址
     */
    private String photoUrl;



    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
