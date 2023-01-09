package com.runjian.auth.server.config;

import com.runjian.auth.server.filter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName WebSecurityConfiguration
 * @Description 认证服务器服务安全相关配置
 * @date 2022-12-26 周一 22:06
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 过滤器链
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 允许跨域 关闭 csrf
        http = http.cors().and().csrf().disable();
        // 关闭 session
        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
        // 配置登入认证失败、权限认证失败异常处理器
        http = http.exceptionHandling().authenticationEntryPoint(
                (request, response, ex) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                }
        ).and();

        http.authorizeRequests()
                // 对于验证码、登录接口 允许匿名访问
                .antMatchers("/user/login").anonymous()
                .antMatchers("/captchaImage").anonymous()
                // 放行 swagger knife4j，druid资源
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/profile/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/v3/**").permitAll()
                .antMatchers("/doc.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/druid/**").permitAll()
                .antMatchers("/**/*.js").permitAll()
                .antMatchers("/**/*.css").permitAll()
                .antMatchers("/**/*.png").permitAll()
                .antMatchers("/**/*.ico").permitAll()

                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().fullyAuthenticated();

        // 把token校验过滤器添加到过滤链中
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

}
