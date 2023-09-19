package com.runjian.alarm.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/9/19 11:28
 */
@Data
public class PutAlarmSchemeDisabledReq {

    /**
     * 预案id
     */
    @NotNull(message = "预案id不能为空")
    @Range(min = 1, max = 99999999L, message = "预案id不正确")
    private Long id;

    /**
     * 是否禁用
     */
    @NotNull(message = "是否禁用不能为空")
    @Range(min = 0, max = 1, message = "是否禁用不正确")
    private Integer disabled;
}
