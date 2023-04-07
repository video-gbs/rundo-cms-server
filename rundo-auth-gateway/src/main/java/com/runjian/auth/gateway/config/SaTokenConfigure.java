package com.runjian.auth.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SaTokenConfigure
 * @Description
 * @date 2023-02-28 周二 10:24
 */
@Slf4j
@Configuration
public class SaTokenConfigure {

    /**
     * 注册 Sa-Token全局过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")
                // 开放地址
                .addExclude(
                        "/favicon.ico",
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources",
                        "/swagger-resources/**",

                        "/auth/**",
                        "/auth/v3/**",

                        "/expserver/**",
                        "/expserver/v3/**",

                        "/stream-manage/**",
                        "/device-control/**"
                )
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
                    SaRouter.match("/**","/auth/user/login").check(r -> StpUtil.checkLogin());
                    Object loginId = StpUtil.getLoginId();

                    // 角色认证
                    // SaRouter.match("/admin/**",  r -> StpUtil.checkRoleOr("admin","super-admin"));

                    // 权限认证 -- 不同模块, 校验不同权限
                    // SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
                    // SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
                    // SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
                    // SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));

                    // 更多匹配 ...  */
                });
    }
}
