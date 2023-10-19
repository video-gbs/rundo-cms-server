package com.runjian.alarm.vo.request;

import com.runjian.alarm.entity.relation.AlarmSchemeEventRel;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Miracle
 * @date 2023/9/19 11:04
 */
@Data
public class PutAlarmSchemeEventReq {

    /**
     * 预案id
     */
    @NotNull(message = "预案id不能为空")
    @Range(min = 1, max = 999999999, message = "预案id不正确")
    private Long schemeId;

    /**
     * 事件编码
     */
    @NotBlank(message = "事件编码不能为空")
    @Size(min = 1, max = 100, message = "事件编码长度不正确")
    private String eventCode;

    /**
     * 事件等级
     */
    @NotNull(message = "事件等级不能为空")
    @Range(min = 1, max = 4, message = "事件等级不正确")
    private Integer eventLevel;

    /**
     * 事件重复触发间隔时间 单位：秒 3、5、10、30
     */
    @NotNull(message = "事件重复触发间隔时间不能为空")
    @Range(min = 0, max = 60, message = "事件重复触发间隔时间不正确")
    private Integer eventInterval;

    /**
     * 是否开启视频
     */
    @NotNull(message = "是否开启视频不能为空")
    @Range(min = 0, max = 1, message = "是否开启视频不正确")
    private Integer enableVideo;

    /**
     * 录像时间 单位：秒 15、30、60
     */
    @NotNull(message = "录像时间不能为空")
    @Range(min = 15, max = 60, message = "录像时间不正确")
    private Integer videoLength;

    /**
     * 视频是否有音频
     */
    @NotNull(message = "视频是否有音频不能为空")
    @Range(min = 0, max = 1, message = "视频是否有音频不正确")
    private Integer videoHasAudio;

    /**
     * 是否开启图片截图
     */
    @NotNull(message = "是否开启图片截图不能为空")
    @Range(min = 0, max = 1, message = "是否开启图片截图不正确")
    private Integer enablePhoto;

    public AlarmSchemeEventRel toAlarmSchemeEventRel(){
        AlarmSchemeEventRel alarmSchemeEventRel = new AlarmSchemeEventRel();
        BeanUtils.copyProperties(this, alarmSchemeEventRel);
        return alarmSchemeEventRel;
    }
}
