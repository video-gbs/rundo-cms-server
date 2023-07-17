package com.runjian.device.expansion.feign.fallback;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.device.expansion.feign.AutRbacServerApi;
import com.runjian.device.expansion.feign.AuthServerApi;
import com.runjian.device.expansion.vo.feign.request.PostBatchResourceReq;
import com.runjian.device.expansion.vo.feign.request.PutResourceReq;
import com.runjian.device.expansion.vo.feign.response.GetResourceTreeRsp;
import com.runjian.device.expansion.vo.feign.response.VideoAreaResp;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 调用失败处理工厂 熔断
 *
 * @author huangtongkui
 */
@Slf4j
@Component
public class AuthRbacServerApiFallbackFactory implements FallbackFactory<AutRbacServerApi> {

    @Override
    public AutRbacServerApi create(Throwable throwable) {
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
        return new AutRbacServerApi() {

            @Override
            public CommonResponse<?> batchAddResource(PostBatchResourceReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--batchAddResource操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<?> updateResource(PutResourceReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--updateResource操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<?> delete(Long resourceId) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--delete操作失败",resourceId, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<GetResourceTreeRsp> getResourcePage(String resourceKey, Boolean isIncludeResource) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--getResourcePage操作失败",resourceKey, throwable);
                return (CommonResponse<GetResourceTreeRsp>) finalFailure;
            }
        };
    }
}
