package com.runjian.device.expansion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class DefaultMvcConfig extends WebMvcConfigurationSupport {


    @Override
    protected void configurePathMatch(PathMatchConfigurer configurer) {
        //setUseSuffixPatternMatch:设置是否遵循后缀匹配模式，如“/user”是否匹配/user.*，为true时就匹配;
        configurer.setUseTrailingSlashMatch(false);
    }


    /**
     * 释放swagger的请求
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html","doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}


