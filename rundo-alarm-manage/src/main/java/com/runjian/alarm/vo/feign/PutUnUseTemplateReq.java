package com.runjian.alarm.vo.feign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 解除使用模板请求体
 * @author Miracle
 * @date 2023/9/7 17:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutUnUseTemplateReq {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务使用标志
     */
    private String serviceUseMark;
}
