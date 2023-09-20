package com.runjian.alarm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Miracle
 * @date 2023/9/20 14:57
 */
@Component
public class AlarmProperties {


    @Value("${alarm.file.store.url}")
    private String fileStoreUrl;


}
