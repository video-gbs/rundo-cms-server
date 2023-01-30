package com.runjian.auth.server.common;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName MyBatisPlusConfig
 * @Description MyBatisPlus配置类
 * @date 2023-01-03 周二 14:33
 */
@Configuration
public class MyBatisPlusConfig {


    /**
     * 监测sql执行效率的插件,生产记得注释掉
     *
     * @return
     */
    // @Bean
    // public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
    //     return new PerformanceMonitorInterceptor();
    // }

    /**
     * MybatisPlus分页配置
     *
     * @return
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        return new PaginationInnerInterceptor();
    }

}
