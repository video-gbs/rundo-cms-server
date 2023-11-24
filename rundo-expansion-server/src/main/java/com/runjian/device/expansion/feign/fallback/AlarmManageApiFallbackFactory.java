package com.runjian.device.expansion.feign.fallback;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.common.constant.MarkConstant;
import com.runjian.device.expansion.feign.AlarmManageApi;
import com.runjian.device.expansion.vo.feign.response.GetAlarmChannelRsp;
import com.runjian.device.expansion.vo.feign.response.PageListResp;
import com.runjian.device.expansion.vo.response.GetAlarmDeployChannelRsp;
import com.runjian.device.expansion.vo.response.GetAlarmMsgChannelRsp;
import com.runjian.device.expansion.vo.response.GetAlarmSchemeChannelRsp;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * @author Miracle
 * @date 2023/10/17 15:12
 */
@Slf4j
@Component
public class AlarmManageApiFallbackFactory implements FallbackFactory<AlarmManageApi> {

    @Override
    public AlarmManageApi create(Throwable cause) {
        String message = cause.getMessage();
        CommonResponse<?> failure = null;
        if(cause instanceof FeignException){
            //服务内部的错误
            if(message.contains(MarkConstant.MARK_FEIGN_RESP_CODE)){
                //获取数据具体报错信息
                String errorDetail = message.substring(message.indexOf(MarkConstant.MARK_FEIGN_RESP_START));
                failure = CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_EXCEPTION.getErrCode(),errorDetail,null);
            }else {
                failure = CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
            }
        }else if(cause instanceof TimeoutException){
            //服务请求超时
            failure = CommonResponse.failure(BusinessErrorEnums.FEIGN_TIMEOUT_EXCEPTION);
        }else {
            failure = CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
        }

        final CommonResponse<?> finalFailure = failure;
        return new AlarmManageApi() {
            @Override
            public CommonResponse<PageListResp<GetAlarmMsgChannelRsp>> getAlarmMsgPage(int page, int num, Long channelId, String alarmCode, LocalDateTime alarmStartTime, LocalDateTime alarmEndTime, List<Long> channelIds) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"告警服务","告警消息获取失败", cause.getMessage(), cause);
                return (CommonResponse<PageListResp<GetAlarmMsgChannelRsp>>)finalFailure;
            }

            @Override
            public CommonResponse<PageListResp<GetAlarmDeployChannelRsp>> getChannelDeploy(int page, int num, Long schemeId) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"告警服务","告警消息获取失败", cause.getMessage(), cause);
                return (CommonResponse<PageListResp<GetAlarmDeployChannelRsp>>)finalFailure;
            }

            @Override
            public CommonResponse<List<GetAlarmChannelRsp>> getAlarmChannel(Set<Long> channelIds) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"告警服务","告警消息获取失败", cause.getMessage(), cause);
                return (CommonResponse<List<GetAlarmChannelRsp>>)finalFailure;
            }
        };
    }
}
