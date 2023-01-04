package com.runjian.auth.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName WebSecurityConfiguration
 * @Description 认证服务器服务安全相关配置
 * @date 2022-12-26 周一 22:06
 */
@Configuration
public class SecurityConfig {

    /**
     * 用于身份验证安全过滤器链
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().csrf().disable()
                .build();
    }

}
