package com.runjian.auth.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;

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
                .addExclude("/**")
                // 开放地址
                .addExclude("/auth/swagger-ui",
                        "/auth/swagger-resources",
                        "/auth/profile",
                        "/auth/v2",
                        "/auth/v3",
                        "/auth/doc.html",
                        "/auth/webjars",
                        "/auth/druid",
                        "/auth/api/v1"
                )
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
                    SaRouter.match("/**", "/auth/user/login", r -> StpUtil.checkLogin());

                    // 角色认证
                    // SaRouter.match("/admin/**",  r -> StpUtil.checkRoleOr("admin","super-admin"));

                    // 权限认证 -- 不同模块, 校验不同权限
                    // SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
                    // SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
                    // SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
                    // SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));

                    // 更多匹配 ...  */
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                    ServerWebExchange exchange = SaReactorSyncHolder.getContext();
                    exchange.getResponse().getHeaders().set("Content-Type", "application/json; charset=utf-8");
                    return SaResult.error(e.getMessage());
                })
                .setBeforeAuth(obj -> {
                    SaHolder.getResponse()
                            //
                            .setHeader("Access-Control-Allow-Origin", "*")
                            .setHeader("Access-Control-Allow-Methods", "GET,HEAD,POST,PUT,PATCH,DELETE,OPTIONS,TRACE")
                            .setHeader("Access-Control-Max-Age", "3600")
                            .setHeader("Access-Control-Headers", "*");
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> log.info("OPTIONS预检请求，不做处理"))
                            .back();
                });
    }
}
