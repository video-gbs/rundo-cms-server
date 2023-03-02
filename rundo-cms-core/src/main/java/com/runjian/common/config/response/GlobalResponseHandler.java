package com.runjian.common.config.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Miracle
 * @date 2022/9/16 10:16
 */
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Autowired
    private ObjectMapper objectMapper;

    private static String[] excludeUrls;

    /**
     * 排除无需进行统一返回的url
     *
     * @param environment
     */
    private GlobalResponseHandler(Environment environment) {
        String excludeUrl = environment.getProperty("response.exclude-url");
        if (StringUtils.hasText(excludeUrl)) {
            excludeUrls = excludeUrl.split(",");
        } else {
            excludeUrls = new String[0];
        }
    }


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果接口返回的类型本身就是CommonResponse那就没有必要进行额外的操作，返回false
        return !returnType.getGenericParameterType().equals(CommonResponse.class);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> clazz,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 排除api文档的接口，这个接口不需要统一
        for (String path : excludeUrls) {
            if (request.getURI().getPath().startsWith(path)) {
                return body;
            }
        }
        if (body instanceof String) {
            return objectMapper.writeValueAsString(CommonResponse.success(body));
        }

        if (body instanceof CommonResponse) {
            return body;
        }
        return CommonResponse.success(body);
    }
}
