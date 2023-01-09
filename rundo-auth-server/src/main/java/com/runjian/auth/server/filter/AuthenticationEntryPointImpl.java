package com.runjian.auth.server.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AuthenticationEntryPointImpl
 * @Description 登录认证失败处理器
 * @date 2023-01-09 周一 3:39
 */

@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // WebUtils.renderString(response, JSON.toJSONString());
    }
}
