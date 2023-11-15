package com.runjian.device.expansion.feign.fallback;

import com.alibaba.fastjson2.JSONObject;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.device.expansion.feign.StreamManageApi;
import com.runjian.device.expansion.vo.feign.request.*;
import com.runjian.device.expansion.vo.feign.response.GetDispatchNameRsp;
import com.runjian.device.expansion.vo.feign.response.StreamInfo;
import com.runjian.device.expansion.vo.request.RecordStreamOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamSeekOperationReq;
import com.runjian.device.expansion.vo.request.RecordStreamSpeedOperationReq;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
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
            public CommonResponse<StreamInfo> play(PlayFeignReq playFeignReq) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--操作失败",playFeignReq, throwable);
                return (CommonResponse<StreamInfo>) finalFailure;
            }

            @Override
            public CommonResponse<StreamInfo> playBack(PlayBackFeignReq playBackReq) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--操作失败",playBackReq, throwable);
                return (CommonResponse<StreamInfo>) finalFailure;
            }

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

            @Override
            public CommonResponse<?> stopPlay(PutStreamOperationReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--stopPlay操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<List<GetDispatchNameRsp>> getDispatchName() {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--getDispatchName操作失败",null, throwable);
                return (CommonResponse<List<GetDispatchNameRsp>>) finalFailure;
            }

            @Override
            public CommonResponse<Object> getDispatchByPage(Integer page, Integer num, String name) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--getDispatchByPage操作失败",name, throwable);
                return (CommonResponse<Object>) finalFailure;
            }

            @Override
            public CommonResponse<?> updateExtraData(PutDispatchExtraDataReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--updateExtraData操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<Long> getDispatchIdByGatewayId(Long gatewayId) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--getDispatchIdByGatewayId操作失败",gatewayId, throwable);
                return (CommonResponse<Long>) finalFailure;
            }

            @Override
            public CommonResponse<Object> getGatewayByDispatchIdIn(int page, int num, Long dispatchId, String name) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--getGatewayByDispatchIdIn操作失败",name, throwable);
                return (CommonResponse<Object>) finalFailure;
            }

            @Override
            public CommonResponse<Object> getGatewayByDispatchIdNotIn(int page, int num, Long dispatchId, String name) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--getGatewayByDispatchIdNotIn操作失败",name, throwable);
                return (CommonResponse<Object>) finalFailure;
            }

            @Override
            public CommonResponse<?> gatewayBindingDispatch(PostGatewayBindingDispatchReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--gatewayBindingDispatch操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<?> dispatchBindingGateway(PostDispatchBindingGatewayReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--dispatchBindingGateway操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<?> dispatchUnBindingGateway(PostDispatchBindingGatewayReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--dispatchUnBindingGateway操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<String> webrtcAudio(WebRtcAudioReq webRtcAudioReq) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"中心调度服务","feign--dispatchUnBindingGateway操作失败",webRtcAudioReq, throwable);
                return (CommonResponse<String>) finalFailure;
            }
        };
    }
}
