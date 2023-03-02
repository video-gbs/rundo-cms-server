// package com.runjian.common.config.jackson;
//
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
// import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.stereotype.Component;
//
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
//
// /**
//  * @author Jiang4Yu
//  * @version V1.0.0
//  * @ClassName JavaTimeAutoConfiguration
//  * @Description
//  * @date 2023-02-21 周二 15:47
//  */
// @Component
// @ConditionalOnClass(JavaTimeModule.class)
// public class JavaTimeAutoConfiguration {
//
//     @Value("${spring.jackson.date-format}")
//     private String dateFormat;
//
//     @Bean
//     @ConditionalOnProperty("spring.jackson.date-format")
//     Jackson2ObjectMapperBuilderCustomizer customizeLocalDateTimeFormat() {
//         return jacksonObjectMapperBuilder -> {
//             DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
//
//             jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
//             jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
//         };
//     }
// }
//
