// package com.runjian.auth.server.config;
//
// import com.runjian.auth.server.filter.JwtAuthenticationTokenFilter;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
// /**
//  * @author Jiang4Yu
//  * @version V1.0.0
//  * @ClassName WebSecurityConfiguration
//  * @Description 认证服务器服务安全相关配置
//  * @date 2022-12-26 周一 22:06
//  */
// @Configuration
// public class SecurityConfig {
//
//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
//
//     @Autowired
//     private AuthenticationConfiguration authenticationConfiguration;
//
//     @Autowired
//     private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
//
//     @Bean
//     public AuthenticationManager authenticationManagerBean() throws Exception {
//         return authenticationConfiguration.getAuthenticationManager();
//     }
//
//     @Bean
//     public WebSecurityCustomizer webSecurityCustomizer() {
//         return web -> web.ignoring().antMatchers(
//                 "/favicon.ico",
//                 "/swagger-ui.html",
//                 "/doc.html",
//                 "/swagger-ui/**",
//                 "/swagger-resources/**",
//                 "/v2/api-docs",
//                 "/v3/api-docs",
//                 "/webjars/**"
//         );
//     }
//
//
//     @Bean
//     protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//                 // 关闭csrf
//                 .csrf().disable()
//                 // 不通过Session获取SecurityContext
//                 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                 .and()
//                 .authorizeRequests()
//                 // 对于登录接口 允许匿名访问
//                 .antMatchers("/login").permitAll()
//                 // 对于验证码接口 允许匿名访问
//                 .antMatchers("/captchaImage").anonymous()
//                 // 除上面外的所有请求全部需要鉴权认证
//                 .anyRequest().authenticated();
//         // 把token校验过滤器添加到过滤器链中
//         http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//
//         return http.build();
//     }
// }
