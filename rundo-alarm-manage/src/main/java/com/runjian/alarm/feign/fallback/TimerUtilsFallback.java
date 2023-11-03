package com.runjian.alarm.feign.fallback;

import com.runjian.alarm.feign.TimerUtilsApi;
import com.runjian.alarm.vo.feign.PostUseTemplateReq;
import com.runjian.alarm.vo.feign.PutUnUseTemplateReq;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/10/31 10:46
 */
@Slf4j
@Component
public class TimerUtilsFallback implements FallbackFactory<TimerUtilsApi> {
    @Override
    public TimerUtilsApi create(Throwable cause) {
        return new TimerUtilsApi() {
            @Override
            public CommonResponse<Boolean> checkTime(Long templateId, String time) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "时间模板工具接口调用服务", "接口调用失败", cause.getMessage(), String.format("templateId:%s,time:%s", templateId, time));
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<?> useTemplate(PostUseTemplateReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "时间模板工具接口调用服务", "接口调用失败", cause.getMessage(), req);
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<?> unUseTemplate(PutUnUseTemplateReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "时间模板工具接口调用服务", "接口调用失败", cause.getMessage(), req);
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }
        };
    }
}
