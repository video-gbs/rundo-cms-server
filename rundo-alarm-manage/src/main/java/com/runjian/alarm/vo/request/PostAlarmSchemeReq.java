package com.runjian.alarm.vo.request;

import com.runjian.alarm.entity.relation.AlarmSchemeEventRel;
import com.runjian.common.validator.constraints.annotation.NotSpecialChar;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/9/19 10:56
 */
@Data
public class PostAlarmSchemeReq {

    /**
     * 预案名称
     */
    @NotSpecialChar(except = {"-"},message = "预案名称不能包含特殊字符")
    @NotBlank(message =  "预案名称不能为空")
    private String schemeName;

    /**
     * 时间模板id
     */
    @NotNull(message = "时间模板id不能为空")
    @Range(min = 1, max = 999999999, message = "时间模板id不正确")
    private Long templateId;

    /**
     * 通道id数组
     */
    @Size(max = 999999999, message = "通道id数组不正确")
    private Set<Long> channelIds;

    /**
     * 告警预案时间数组
     */
    @NotNull(message = "告警预案时间数组不能为空")
    @Size(min = 1, max = 999999999, message = "通道id数组不正确")
    private List<PostAlarmSchemeEventReq> alarmSchemeEventReqList;

    public List<AlarmSchemeEventRel> getAlarmSchemeEventRelList() {
        return alarmSchemeEventReqList.stream().map(PostAlarmSchemeEventReq::toAlarmSchemeEventRel).collect(Collectors.toList());
    }
}
