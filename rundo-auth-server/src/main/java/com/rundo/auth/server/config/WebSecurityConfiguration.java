package com.rundo.auth.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName WebSecurityConfiguration
 * @Description 认证服务器服务安全相关配置
 * @date 2022-12-26 周一 22:06
 */
@EnableWebSecurity
public class WebSecurityConfiguration {
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests -> authorizeRequests
                        // 开放自定义的部分端点
                        .antMatchers("/token/*").permitAll()
                        .anyRequest().authenticated()
                )
                // 避免iframe同源无法登录
                .headers().frameOptions().sameOrigin()
                .and()
                // 表单登录个性化
                .apply(new FormLoginConfigurer<>());
        return null;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().antMatchers("/css/**", "/error");
    }

}
