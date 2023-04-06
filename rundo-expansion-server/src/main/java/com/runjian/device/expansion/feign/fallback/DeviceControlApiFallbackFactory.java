package com.runjian.device.expansion.feign.fallback;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.vo.feign.request.*;
import com.runjian.device.expansion.vo.feign.response.*;
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
public class DeviceControlApiFallbackFactory implements FallbackFactory<DeviceControlApi> {

    @Override
    public DeviceControlApi create(Throwable throwable) {
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

        return new DeviceControlApi() {
            @Override
            public CommonResponse<DeviceAddResp> deviceAdd(DeviceReq deviceReq) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加失败",deviceReq, throwable);
                return (CommonResponse<DeviceAddResp>) finalFailure;
            }

            @Override
            public CommonResponse deviceSignSuccess(PutDeviceSignSuccessReq putDeviceSignSuccessReq) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器注册状态修改",putDeviceSignSuccessReq, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse deleteDevice(Long id) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加删除",id, throwable);
                return finalFailure;
            }

            @Override
            public CommonResponse<Boolean> channelSignSuccess(PutChannelSignSuccessReq putChannelSignSuccessReq) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--通道添加失败",putChannelSignSuccessReq, throwable);
                return (CommonResponse<Boolean>) finalFailure;
            }

            @Override
            public CommonResponse<PageListResp<GetChannelByPageRsp>> getChannelByPage(int page, int num, String nameOrOriginId) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--通道待添加列表获取失败",nameOrOriginId, throwable);
                return (CommonResponse<PageListResp<GetChannelByPageRsp>>) finalFailure;
            }

            @Override
            public CommonResponse<ChannelSyncRsp> channelSync(Long deviceId) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--通道同步获取失败",deviceId, throwable);
                return (CommonResponse<ChannelSyncRsp>) finalFailure;
            }

            @Override
            public CommonResponse<Boolean> channelDelete(List<Long> channelId) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--通道删除失败",channelId, throwable);
                return (CommonResponse<Boolean>) finalFailure;
            }

            @Override
            public CommonResponse<StreamInfo> play(PlayFeignReq playFeignReq) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--通道播放失败", playFeignReq, throwable);
                return (CommonResponse<StreamInfo>) finalFailure;
            }

            @Override
            public CommonResponse<StreamInfo> playBack(PlayBackFeignReq playBackReq) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--通道播放失败",playBackReq, throwable);
                return (CommonResponse<StreamInfo>) finalFailure;
            }
        };
    }
}
