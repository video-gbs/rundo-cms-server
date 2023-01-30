package com.runjian.auth.server.common;

import com.baomidou.mybatisplus.annotation.DbType;
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
     * MybatisPlus分页配置
     *
     * @return
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        return new PaginationInnerInterceptor(DbType.MYSQL);
    }

}
