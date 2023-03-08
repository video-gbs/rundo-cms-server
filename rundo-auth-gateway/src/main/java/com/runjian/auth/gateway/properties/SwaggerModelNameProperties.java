package com.runjian.auth.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SwaggerModelNameProperties
 * @Description
 * @date 2023-02-28 周二 18:57
 */
@Data
@Component
@ConfigurationProperties(prefix = "swagger.gateway")
public class SwaggerModelNameProperties {
    List<Map<String, String>> modelName;
}
