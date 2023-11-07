package com.runjian.alarm.vo.dto;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/11/7 9:57
 */
@Data
public class AlarmSchemeEventDto {

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
     * 事件名称
     */
    private String eventName;

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


}
