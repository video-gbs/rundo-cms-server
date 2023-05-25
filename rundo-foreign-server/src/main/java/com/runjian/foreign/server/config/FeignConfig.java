package com.runjian.foreign.server.config;

import com.runjian.common.constant.CommonConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Feign配置
 * 使用FeignClient进行服务间调用，传递headers信息
 * @author chenjialing
 */
@Configuration
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headersNames = request.getHeaderNames();
        while (headersNames.hasMoreElements()){
            String name = headersNames.nextElement();
            if(CommonConstant.AUTHORIZATION.equalsIgnoreCase(name)){
                //添加token
                requestTemplate.header(CommonConstant.AUTHORIZATION, request.getHeader(name));
            }
        }

    }
}