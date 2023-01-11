package com.runjian.auth.server.handler;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.util.WebUtils;
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
 * @Description
 * @date 2023-01-09 周一 16:56
 */
@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.HTTP_UNAUTHORIZED, "登录失败，请稍后重试", null);
        String json = JSONUtil.toJsonStr(result);
        WebUtils.renderString(response, json);
    }
}
