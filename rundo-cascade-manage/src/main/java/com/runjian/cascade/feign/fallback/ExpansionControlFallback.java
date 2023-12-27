package com.runjian.cascade.feign.fallback;

import com.runjian.cascade.feign.DeviceControlApi;
import com.runjian.cascade.feign.ExpansionControlApi;
import com.runjian.cascade.vo.feign.resp.ChannelExpansionResp;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/3/3 15:12
 */
@Component
public class ExpansionControlFallback implements FallbackFactory<ExpansionControlApi> {
    @Override
    public ExpansionControlApi create(Throwable cause) {
        return new ExpansionControlApi() {

            @Override
            public CommonResponse<List<ChannelExpansionResp>> getListBatch(List<Long> channelIds) {
                return CommonResponse.create(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR.getErrCode(), cause.getMessage(), null);
            }
        };
    }
}
