package com.runjian.alarm.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.runjian.alarm.entity.relation.AlarmSchemeEventRel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/9/11 10:50
 */
@Data
public class GetAlarmSchemeRsp {

    /**
     * 预案id
     */
    private Long id;

    /**
     * 预案名称
     */
    private String schemeName;

    /**
     * 事件模板id
     */
    private Long templateId;

    /**
     * 通道id
     */
    private List<Long> channelIdList;

    /**
     * 告警关联事件
     */
    private List<GetAlarmSchemeEventRsp> alarmSchemeEventRelList;


    /**
     * 是否禁用
     */
    private Integer disabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
