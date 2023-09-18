package com.runjian.parsing.feign.fallback;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.feign.AlarmManageApi;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Miracle
 * @date 2023/9/18 17:07
 */
@Component
public class AlarmManageFallback implements FallbackFactory<AlarmManageApi> {
    @Override
    public AlarmManageApi create(Throwable cause) {
        return new AlarmManageApi() {
            @Override
            public CommonResponse<?> receiveAlarmMsg(JSONObject jsonObject) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), jsonObject);
            }
        };
    }
}
