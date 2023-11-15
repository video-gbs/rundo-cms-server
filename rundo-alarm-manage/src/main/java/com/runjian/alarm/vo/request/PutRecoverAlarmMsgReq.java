package com.runjian.alarm.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 告警恢复请求体
 * @author Miracle
 * @date 2023/11/3 16:04
 */
@Data
public class PutRecoverAlarmMsgReq {

    /**
     * 告警信息id
     */
    @NotNull(message = "告警信息id不能为空")
    private Long alarmMsgId;

    /**
     * 告警文件类型
     */
    @NotNull(message = "告警文件类型不能为空")
    @Range(min = 1, max = 2, message = "告警文件类型不正确")
    private Integer alarmFileType;
}
