package com.runjian.alarm.vo.request;

import com.runjian.alarm.entity.relation.AlarmSchemeEventRel;
import com.runjian.common.validator.constraints.annotation.NotSpecialChar;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 告警预案添加请求体
 * @author Miracle
 * @date 2023/9/19 11:31
 */
@Data
public class PutAlarmSchemeReq {

    /**
     * 预案id
     */
    @NotNull(message = "预案id不能为空")
    @Range(min = 1, max = 999999999, message = "预案id不正确")
    private Long id;

    /**
     * 预案名称
     */
    @NotBlank(message =  "预案名称不能为空")
    @NotSpecialChar(except = {"-"},message = "预案名称不能包含特殊字符")
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
    @NotNull(message = "告警预案事件数组不能为空")
    @Size(min = 1, max = 999999999, message = "通道id数组不正确")
    private List<PutAlarmSchemeEventReq> alarmSchemeEventReqList;

    public List<AlarmSchemeEventRel> getAlarmSchemeEventRelList() {
        return alarmSchemeEventReqList.stream().map(putAlarmSchemeEventReq -> {
            AlarmSchemeEventRel alarmSchemeEventRel = new AlarmSchemeEventRel();
            BeanUtils.copyProperties(putAlarmSchemeEventReq, alarmSchemeEventRel);
            alarmSchemeEventRel.setSchemeId(this.id);
            return alarmSchemeEventRel;
        }).collect(Collectors.toList());
    }
}
