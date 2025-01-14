package com.runjian.device.feign.fallback;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.feign.ParsingEngineApi;
import com.runjian.device.vo.feign.DeviceControlReq;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Miracle
 * @date 2023/3/3 11:32
 */
@Component
public class ParsingEngineFallback implements FallbackFactory<ParsingEngineApi> {


    @Override
    public ParsingEngineApi create(Throwable cause) {
        return new ParsingEngineApi() {
            @Override
            public CommonResponse<?> customEvent(DeviceControlReq req) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<?> deviceTotalSync(Set<Long> gatewayIds) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }
        };
    }
}
