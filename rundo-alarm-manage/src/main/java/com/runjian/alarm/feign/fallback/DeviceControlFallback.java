package com.runjian.alarm.feign.fallback;

import com.runjian.alarm.feign.DeviceControlApi;
import com.runjian.alarm.vo.request.PutDefenseReq;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Set;

/**
 * @author Miracle
 * @date 2023/10/31 10:44
 */
@Slf4j
public class DeviceControlFallback implements FallbackFactory<DeviceControlApi> {
    @Override
    public DeviceControlApi create(Throwable cause) {
        return new DeviceControlApi() {
            @Override
            public CommonResponse<Set<Long>> defense(PutDefenseReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "设备控制接口调用服务", "接口调用失败", cause.getMessage(), req);
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }
        };
    }
}
