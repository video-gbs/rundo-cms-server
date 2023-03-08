package com.runjian.auth.server.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SatokenInterceptor
 * @Description
 * @date 2023-02-28 周二 14:26
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义认证规则
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
            SaRouter.match("/**")
                    .notMatch("/user/login")
                    .notMatch("/user/code")
                    // 开放文档地址
                    .notMatch("/**/swagger-resources",
                            "/**/v3/api-docs/**",
                            "/**/doc.html"
                    ).check(StpUtil::checkLogin);
            // 角色校验 -- 拦截以 admin 开头的路由，必须具备 admin 角色或者 super-admin 角色才可以通过认证
            // SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr("admin", "super-admin"));
            // 权限校验 -- 不同模块校验不同权限
            // SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
            // SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
            // SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
            // SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
            // SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
            // SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));
        }));
    }
}
