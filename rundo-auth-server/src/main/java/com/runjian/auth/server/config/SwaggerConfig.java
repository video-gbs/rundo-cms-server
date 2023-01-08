// package com.runjian.auth.server.config;
//
// import io.swagger.annotations.ApiOperation;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import springfox.documentation.builders.ApiInfoBuilder;
// import springfox.documentation.builders.PathSelectors;
// import springfox.documentation.builders.RequestHandlerSelectors;
// import springfox.documentation.oas.annotations.EnableOpenApi;
// import springfox.documentation.service.ApiInfo;
// import springfox.documentation.service.Contact;
// import springfox.documentation.spi.DocumentationType;
// import springfox.documentation.spring.web.plugins.Docket;
//
// /**
//  * @author Jiang4Yu
//  * @version V1.0.0
//  * @ClassName SwaggerConfig
//  * @Description Swagger配置
//  * @date 2023-01-06 周五 18:14
//  */
// @Configuration
// @EnableOpenApi
// public class SwaggerConfig {
//
//     @Value("${knife4j.enable}")
//     private Boolean enable;
//
//
//     @Value("${knife4j.openapi.title}")
//     private String title;
//
//     @Value("${knife4j.openapi.concat}")
//     private String concat;
//
//     @Value("${knife4j.openapi.email}")
//     private String email;
//     @Value("${knife4j.openapi.url}")
//     private String url;
//
//     @Value("${knife4j.openapi.description}")
//     private String description;
//
//     @Value("${knife4j.openapi.version}")
//     private String version;
//
//
//     @Bean
//     public Docket createRestApi() {
//         return new Docket(DocumentationType.OAS_30).pathMapping("/")
//                 // 配置网站的基本信息
//                 .apiInfo(apiInfo())
//                 .enable(enable).select()
//                 // 扫描所有有注解的api，用这种方式更灵活
//                 .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                 .paths(PathSelectors.any())
//                 .build();
//
//     }
//
//
//     private ApiInfo apiInfo() {
//         return new ApiInfoBuilder()
//                 .title(title)
//                 .description(description)
//                 .contact(new Contact(concat, url, email))
//                 .version(version)
//                 .build();
//     }
//
//
// }
