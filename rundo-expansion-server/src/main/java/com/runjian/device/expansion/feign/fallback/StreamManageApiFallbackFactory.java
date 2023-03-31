package com.runjian.device.expansion.feign.fallback;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.feign.StreamManageApi;
import com.runjian.device.expansion.vo.request.RecordStreamOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamSeekOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamSpeedOperationReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StreamManageApiFallbackFactory implements FallbackFactory<StreamManageApi> {
    @Override
    public StreamManageApi create(Throwable throwable) {
        return new StreamManageApi() {
            @Override
            public CommonResponse<Boolean> recordPause(RecordStreamOperationReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--操作失败",req, throwable);
                return CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
            }

            @Override
            public CommonResponse<Boolean> recordResume(RecordStreamOperationReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--操作失败",req, throwable);
                return CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
            }

            @Override
            public CommonResponse<?> recordSpeed(RecordStreamSpeedOperationReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--操作失败",req, throwable);
                return CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
            }

            @Override
            public CommonResponse<?> recordSeek(RecordStreamSeekOperationReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--操作失败",req, throwable);
                return CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
            }
        };
    }
}
