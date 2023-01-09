package com.runjian.auth.server.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName LoginFailureHandler
 * @Description 登入失败处理器（未使用）
 * @date 2023-01-09 周一 3:47
 */
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.info("进入到登入失败处理器，失败原因：{}", exception.getLocalizedMessage());
        if (exception instanceof LockedException) {
            // WebUtil.renderString(response, JSON.toJSONString(ResponseModels.commonException("账户被锁定，请联系管理员!")));
        } else if (exception instanceof CredentialsExpiredException) {
            // WebUtil.renderString(response, JSON.toJSONString(ResponseModels.commonException("密码过期，请联系管理员!")));
        // } else if (exception instanceof AccountExpiredException) {
            // WebUtil.renderString(response, JSON.toJSONString(ResponseModels.commonException("账户过期，请联系管理员!")));
        } else if (exception instanceof DisabledException) {
            // WebUtil.renderString(response, JSON.toJSONString(ResponseModels.commonException("账户被禁用，请联系管理员!")));
        } else if (exception instanceof BadCredentialsException) {
            // WebUtil.renderString(response, JSON.toJSONString(ResponseModels.commonException("用户名或者密码输入错误，请重新输入!")));
        }
    }
}
