package com.runjian.device.expansion.feign.fallback;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import com.runjian.device.expansion.feign.DeviceControlApi;
import com.runjian.device.expansion.vo.feign.request.DeviceReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

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
        return new DeviceControlApi() {
            @Override
            public CommonResponse<Long> deviceAdd(DeviceReq deviceReq) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加失败",deviceReq, throwable);
                return CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
            }

            @Override
            public CommonResponse deleteDevice(Long id) {
                log.error(LogTemplate.ERROR_LOG_MSG_TEMPLATE,"控制服务","feign--编码器添加删除",id, throwable);
                return CommonResponse.failure(BusinessErrorEnums.INTERFACE_INNER_INVOKE_ERROR);
            }
        };
    }
}
