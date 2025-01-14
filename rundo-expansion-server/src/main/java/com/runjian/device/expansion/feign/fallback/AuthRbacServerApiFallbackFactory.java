package com.runjian.device.expansion.feign.fallback;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.device.expansion.feign.AuthRbacServerApi;
import com.runjian.device.expansion.vo.feign.request.*;
import com.runjian.device.expansion.vo.feign.response.GetResourceTreeRsp;
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
public class AuthRbacServerApiFallbackFactory implements FallbackFactory<AuthRbacServerApi> {

    @Override
    public AuthRbacServerApi create(Throwable throwable) {
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
        return new AuthRbacServerApi() {

            @Override
            public CommonResponse<?> batchAddResource(PostBatchResourceReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--batchAddResource操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<?> batchAddResourceKv(PostBatchResourceKvReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--batchAddResourceKv操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<?> updateResourceKv(PutResourceReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--updateResource操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<?> delete(Long resourceId) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--delete操作失败",resourceId, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<Object> getResourcePage(String resourceKey, Boolean isIncludeResource) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--getResourcePage操作失败",resourceKey, throwable);
                return (CommonResponse<Object>) finalFailure;
            }

            @Override
            public CommonResponse<?> fsMove(PutResourceFsMoveReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--dfsMove操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<?> btMoveKv(PutResourceBtMoveReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--btMove操作失败",req, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<List<GetCatalogueResourceRsp>> getCatalogueResourceRsp(Long pid, Boolean isIncludeChild) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--getCatalogueResourceRsp操作失败",pid, throwable);
                return (CommonResponse<List<GetCatalogueResourceRsp>>) finalFailure;
            }

            @Override
            public CommonResponse<List<String>> getAllResource(String resourceKey) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--getAllResource操作失败", resourceKey, throwable);
                return (CommonResponse<List<String>>) finalFailure;
            }

            @Override
            public CommonResponse<?> deleteByResourceValue(String resourceKey, String resourceValue) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--deleteByResourceValue操作失败",resourceValue, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<?> moveResourceValue(ResourceFsMoveKvReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"资源服务","feign--fsMove操作失败",req, throwable);
                return finalFailure;
            }
        };
    }
}
