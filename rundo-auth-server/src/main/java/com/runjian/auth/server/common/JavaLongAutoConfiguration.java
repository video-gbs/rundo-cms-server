package com.runjian.auth.server.common;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName JavaLongAutoConfiguration
 * @Description Jackson全局转化long类型为String，解决jackson序列化时long类型缺失精度问题
 * @date 2023-02-21 周二 18:09
 */
@Configuration
public class JavaLongAutoConfiguration {

    @Bean
    Jackson2ObjectMapperBuilderCustomizer customizeLocalDateTimeFormat() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
    }
}
