package com.runjian.parsing.feign.fallback;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.feign.StreamManageApi;
import com.runjian.parsing.vo.CommonMqDto;
import com.runjian.parsing.vo.request.PostDispatchSignInReq;
import com.runjian.parsing.vo.request.PutDispatchHeartbeatReq;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Miracle
 * @date 2023/3/3 15:17
 */
@Component
public class StreamManageFallback implements FallbackFactory<StreamManageApi> {
    @Override
    public StreamManageApi create(Throwable cause) {
        return new StreamManageApi() {
            @Override
            public CommonResponse<Boolean> dispatchHeartbeat(PutDispatchHeartbeatReq putDispatchHeartbeatReq) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<?> dispatchSignIn(PostDispatchSignInReq req) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<?> streamReceiveResult(JSONObject req) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<?> streamReceiveRecordResult(JSONObject req) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<Boolean> streamCloseHandle(JSONObject req) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public void commonError(CommonMqDto<?> response) {
            }
        };
    }
}
