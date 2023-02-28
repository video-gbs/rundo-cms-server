package com.runjian.auth.server.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SaTokenConfigure
 * @Description
 * @date 2023-02-28 周二 10:53
 */
@Configuration
public class JwtConfigure {
    @Bean
    public StpLogic getStpLogicJwt(){
        return new StpLogicJwtForSimple();
    }
}
