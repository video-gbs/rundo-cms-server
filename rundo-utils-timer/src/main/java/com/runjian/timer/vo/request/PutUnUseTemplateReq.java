package com.runjian.timer.vo.request;

import lombok.Data;

/**
 * 解除使用模板请求体
 * @author Miracle
 * @date 2023/9/7 17:25
 */
@Data
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
