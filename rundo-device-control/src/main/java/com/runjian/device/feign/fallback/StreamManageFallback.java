package com.runjian.device.feign.fallback;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.device.feign.StreamManageApi;
import com.runjian.device.vo.feign.StreamPlayReq;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Miracle
 * @date 2023/3/3 14:56
 */
@Component
public class StreamManageFallback implements FallbackFactory<StreamManageApi> {
    @Override
    public StreamManageApi create(Throwable cause) {
        return new StreamManageApi() {
            @Override
            public CommonResponse<Map<String, Object>> applyPlay(StreamPlayReq req) {
                return CommonResponse.create(BusinessErrorEnums.VALID_NOT_FOUNT_FIELD.getErrCode(), cause.getMessage(), null);
            }
        };
    }
}
