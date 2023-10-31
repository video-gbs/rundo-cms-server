package com.runjian.alarm.feign.fallback;

import com.runjian.alarm.feign.DeviceControlApi;
import com.runjian.alarm.feign.StreamManageApi;
import com.runjian.alarm.vo.request.PostImageDownloadReq;
import com.runjian.alarm.vo.request.PostRecordDownloadReq;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author Miracle
 * @date 2023/10/31 10:45
 */
@Slf4j
public class StreamManageFallback implements FallbackFactory<StreamManageApi> {
    @Override
    public StreamManageApi create(Throwable cause) {
        return new StreamManageApi() {
            @Override
            public CommonResponse<String> applyStreamId(PostRecordDownloadReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "流媒体管理接口调用服务", "接口调用失败", cause.getMessage(), req);
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<String> applyStreamId(PostImageDownloadReq req) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE, "流媒体管理接口调用服务", "接口调用失败", cause.getMessage(), req);
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }
        };
    }
}
