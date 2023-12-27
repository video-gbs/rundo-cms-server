package com.runjian.cascade.feign.fallback;

import com.runjian.cascade.feign.DeviceControlApi;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Miracle
 * @date 2023/3/3 15:12
 */
@Component
public class DeviceControlFallback implements FallbackFactory<DeviceControlApi> {
    @Override
    public DeviceControlApi create(Throwable cause) {
        return new DeviceControlApi() {

        };
    }
}
