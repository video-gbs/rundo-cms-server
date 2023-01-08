package com.runjian.auth.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SwaggerConfig
 * @Description Swagger配置
 * @date 2023-01-06 周五 18:14
 */
@Configuration
public class SwaggerConfig {

    Boolean swaggerEnabled = true;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.runjian.auth.server.controller"))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("权限认证服务")
                .contact(new Contact("oauth", "", ""))
                .version("1.0")
                .description("")
                .build();
    }

}
