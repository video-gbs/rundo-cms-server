package com.runjian.device.expansion.aspect.annotation;

import java.lang.annotation.*;

/**
 * 设备状态更新注解
 * @author chenjialing
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeignResourceRefreshPoint {
}
