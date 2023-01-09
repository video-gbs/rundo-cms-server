package com.runjian.auth.server.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AccessDeniedHandlerImpl
 * @Description 权限认证失败处理器（未使用）
 * @date 2023-01-09 周一 3:43
 */
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        log.error("进入到权限认证失败处理器，失败原因：{}", accessDeniedException.getLocalizedMessage());
        // WebUtil.renderString(response, ResponseModels.noPowerException().toString());
    }
}
