package com.runjian.stream.feign.fallback;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.stream.feign.ParsingEngineApi;
import com.runjian.stream.vo.StreamManageDto;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/3/3 15:22
 */
@Component
public class ParsingEngineFallback implements FallbackFactory<ParsingEngineApi> {

    @Override
    public ParsingEngineApi create(Throwable cause) {
        return new ParsingEngineApi() {
            @Override
            public CommonResponse<Boolean> streamStopPlay(StreamManageDto req) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<Boolean> streamStartRecord(StreamManageDto req) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<Boolean> streamStopRecord(StreamManageDto req) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<List<String>> streamCheckRecordStatus(Long dispatchId, List<String> streamIds) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<List<String>> streamCheckStreamStatus(Long dispatchId, List<String> streamIds) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<?> streamStopAll(Set<Long> dispatchIds) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<?> streamUpdateRecordSpeed(StreamManageDto streamManageDto) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }
        };
    }
}
