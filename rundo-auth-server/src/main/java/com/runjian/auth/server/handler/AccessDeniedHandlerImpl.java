// package com.runjian.auth.server.handler;
//
// import cn.hutool.http.HttpStatus;
// import cn.hutool.json.JSONUtil;
// import com.runjian.auth.server.util.WebUtils;
// import com.runjian.common.config.response.CommonResponse;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.web.access.AccessDeniedHandler;
// import org.springframework.stereotype.Component;
//
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
//
// /**
//  * @author Jiang4Yu
//  * @version V1.0.0
//  * @ClassName AccessDeniedHandlerImpl
//  * @Description
//  * @date 2023-01-09 周一 17:01
//  */
// @Slf4j
// @Component
// public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
//     @Override
//     public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//         CommonResponse commonResponse = CommonResponse.create(HttpStatus.HTTP_FORBIDDEN, "您权限不足", null);
//         String json = JSONUtil.toJsonStr(commonResponse);
//         WebUtils.renderString(response, json);
//     }
// }
