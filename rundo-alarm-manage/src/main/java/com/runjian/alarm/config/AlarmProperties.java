package com.runjian.alarm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Miracle
 * @date 2023/9/20 14:57
 */
@Component
@Data
public class AlarmProperties {

    @Value("${alarm.file.store.path}")
    private String fileStorePath;

    @Value("${alarm.file.upload.url}")
    private String uploadUrl;

    @Value("${alarm.file.nginx.url}")
    private String nginxUrl;
}
