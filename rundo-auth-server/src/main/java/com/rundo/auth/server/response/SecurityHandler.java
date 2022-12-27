package com.rundo.auth.server.response;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName ProjectSecurityHandler
 * @Description 登录后的相关处理
 * @date 2022-12-27 周二 16:10
 */
@Slf4j
@Component
public class SecurityHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, AccessDeniedHandler, LogoutHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 登录成功后处理
     *
     * @param request
     * @param response
     * @param authenticatio
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authenticatio) throws IOException, ServletException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String redirectUrl = savedRequest.getRedirectUrl();
        if (StringUtils.isNotBlank(redirectUrl)) {
            redirectStrategy.sendRedirect(request, response, redirectUrl);
        }
    }

    /**
     * 登录失败后处理
     *
     * @param request
     * @param response
     * @param authenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        log.info("登录失败{}", authenticationException.getMessage());
        CommonResponse<Object> commonResponse = CommonResponse.failure(BusinessErrorEnums.AUTHORIZED_FAILED, authenticationException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(commonResponse.toString());
        response.flushBuffer();

    }

    /**
     * 403处理
     *
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        CommonResponse<Object> commonResponse = CommonResponse.failure(BusinessErrorEnums.AUTHORIZED_FAILED, accessDeniedException.getMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(commonResponse.toString());
        response.flushBuffer();

    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        log.info("注销登录");
    }


}
