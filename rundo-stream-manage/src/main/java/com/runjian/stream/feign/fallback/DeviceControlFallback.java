package com.runjian.stream.feign.fallback;

import com.github.pagehelper.PageInfo;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.stream.feign.DeviceControlApi;
import com.runjian.stream.vo.request.PostGetGatewayByDispatchReq;
import com.runjian.stream.vo.response.GetGatewayByIdsRsp;
import com.runjian.stream.vo.response.GetGatewayRsp;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Miracle
 * @date 2023/3/3 15:21
 */
@Component
public class DeviceControlFallback implements FallbackFactory<DeviceControlApi> {
    @Override
    public DeviceControlApi create(Throwable cause) {
        return new DeviceControlApi() {
            @Override
            public CommonResponse<PageInfo<GetGatewayByIdsRsp>> getGatewayByIds(PostGetGatewayByDispatchReq req) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<PageInfo<GetGatewayByIdsRsp>> getGatewayByName(int page, int num, String name) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }

            @Override
            public CommonResponse<GetGatewayRsp> getGatewayIdByChannelId(Long channelId) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }
        };
    }
}
