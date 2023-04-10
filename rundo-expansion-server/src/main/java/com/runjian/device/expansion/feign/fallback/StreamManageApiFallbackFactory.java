package com.runjian.device.expansion.feign.fallback;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.device.expansion.feign.StreamManageApi;
import com.runjian.device.expansion.vo.feign.request.FeignStreamOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamSeekOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamSpeedOperationReq;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class StreamManageApiFallbackFactory implements FallbackFactory<StreamManageApi> {
    @Override
    public StreamManageApi create(Throwable throwable) {
        String message = throwable.getMessage();
        CommonResponse<?> failure = null;
        if(throwable instanceof FeignException){
            //服务内部的错误
            if(message.contains(MarkConstant.MARK_FEIGN_RESP_CODE)){
                //获取数据具体报错信息
                String errorDetail = message.substring(message.indexOf(MarkConstant.MARK_FEIGN_RESP_START));
                failure = CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_EXCEPTION.getErrCode(),errorDetail,null);
            }else {
                failure = CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
            }
        }else if(throwable instanceof TimeoutException){
            //服务请求超时
            failure = CommonResponse.failure(BusinessErrorEnums.FEIGN_TIMEOUT_EXCEPTION);
        }else {
            failure = CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
        }

        final CommonResponse<?> finalFailure = failure;

        return new StreamManageApi() {
            @Override
            public CommonResponse<Boolean> recordPause(RecordStreamOperationReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--操作失败",req, throwable);
                return (CommonResponse<Boolean>) finalFailure;
            }

            @Override
            public CommonResponse<Boolean> recordResume(RecordStreamOperationReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--操作失败",req, throwable);
                return (CommonResponse<Boolean>) finalFailure;
            }

            @Override
            public CommonResponse<?> recordSpeed(RecordStreamSpeedOperationReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<?> recordSeek(RecordStreamSeekOperationReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<JSONObject> getStreamMediaInfo(FeignStreamOperationReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--操作失败",req, throwable);
                return (CommonResponse<JSONObject>) finalFailure;
            }
        };
    }
}
