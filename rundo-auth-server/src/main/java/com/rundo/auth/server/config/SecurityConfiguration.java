// package com.rundo.auth.server.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;
//
// /**
//  * @author Jiang4Yu
//  * @version V1.0.0
//  * @ClassName WebSecurityConfiguration
//  * @Description 认证服务器服务安全相关配置
//  * @date 2022-12-26 周一 22:06
//  */
// @Configuration
// public class SecurityConfiguration {
//
//     /**
//      * 用于身份验证安全过滤器链
//      *
//      * @param http
//      * @return
//      * @throws Exception
//      */
//     @Bean
//     @Order(2)
//     public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//         http.authorizeRequests(
//                         authorizeRequests -> authorizeRequests
//                                 .anyRequest().authenticated()
//                 )
//                 .formLogin(Customizer.withDefaults())
//                 .csrf().disable();
//         return http.build();
//     }
//
//
//     /**
//      * 用于进行查询用户，验证的用户的UserDetailsService实例
//      *
//      * @return
//      */
//     @Bean
//     public UserDetailsService userDetailsService() {
//         UserDetails userDetails = User
//                 .withUsername("admin")
//                 .password("{noop}123456")
//                 .roles("USER")
//                 .build();
//         return new InMemoryUserDetailsManager(userDetails);
//     }
//
// }
