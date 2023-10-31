package com.runjian.alarm.vo.feign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 使用模板请求体
 * @author Miracle
 * @date 2023/9/7 17:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUseTemplateReq {

    /**
     *  时间模板id
     */
    @NotNull(message = "时间模板id不能为空")
    @Range(min = 1, max = 99999999, message = "时间模板id必须在1-99999999之间")
    private Long templateId;

    /**
     *  服务名称
     */
    @NotBlank(message = "服务名称不能为空")
    private String serviceName;

    /**
     *  服务使用标志
     */
    @NotBlank(message = "服务使用标志不能为空")
    private String serviceUseMark;

    /**
     * 是否启用定时器
     */
    @NotNull(message = "是否启用定时器不能为空")
    @Range(min = 0, max = 1, message = "是否启用定时器必须在0-1之间")
    private Integer enableTimer;
}
