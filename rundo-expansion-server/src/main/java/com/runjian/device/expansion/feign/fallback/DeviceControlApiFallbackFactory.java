package com.runjian.device.expansion.feign.fallback;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.vo.request.feign.DeviceReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

/**
 * 调用失败处理工厂 熔断
 *
 * @author huangtongkui
 */
@Slf4j
@Component
public class DeviceControlApiFallbackFactory implements FallbackFactory<com.runjian.device.feign.DeviceControlApi> {

    @Override
    public com.runjian.device.feign.DeviceControlApi create(Throwable throwable) {
        return new com.runjian.device.feign.DeviceControlApi() {
            @Override
            public CommonResponse<Long> deviceAdd(DeviceReq deviceReq) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加失败",deviceReq, throwable);
                return CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
            }
        };
    }
}
