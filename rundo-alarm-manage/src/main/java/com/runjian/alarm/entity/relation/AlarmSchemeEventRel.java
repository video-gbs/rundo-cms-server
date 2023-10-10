package com.runjian.alarm.entity.relation;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 告警事件信息
 * @author Miracle
 * @date 2023/9/8 11:23
 */
@Data
public class AlarmSchemeEventRel {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 预案id
     */
    private Long schemeId;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 事件等级
     */
    private Integer eventLevel;

    /**
     * 事件重复触发间隔时间 单位：秒 3、5、10、30
     */
    private Integer eventInterval;

    /**
     * 是否开启视频
     */
    private Integer enableVideo;

    /**
     * 录像时间 单位：秒 15、30、60
     */
    private Integer videoLength;

    /**
     * 视频是否有音频
     */
    private Integer videoHasAudio;

    /**
     * 是否开启图片截图
     */
    private Integer enablePhoto;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
