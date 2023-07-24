package com.runjian.device.expansion.config;

import com.runjian.common.constant.CommonConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
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
        if(!ObjectUtils.isEmpty(attributes)){
            //忽略非http的内部调用
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headersNames = request.getHeaderNames();
            while (headersNames.hasMoreElements()){
                String name = headersNames.nextElement();
                if(CommonConstant.AUTHORIZATION.equalsIgnoreCase(name)){
                    //添加token
                    requestTemplate.header(CommonConstant.AUTHORIZATION, request.getHeader(name));
                }else if(CommonConstant.CLINET_ID.equalsIgnoreCase(name)){
                    requestTemplate.header(CommonConstant.CLINET_ID, request.getHeader(name));
                }else if(CommonConstant.IS_ADMIN.equalsIgnoreCase(name)){
                    requestTemplate.header(CommonConstant.IS_ADMIN, request.getHeader(name));
                }else if(CommonConstant.ROLE_IDS.equalsIgnoreCase(name)){
                    requestTemplate.header(CommonConstant.ROLE_IDS, request.getHeader(name));
                }else if(CommonConstant.RECOURCE_KEY.equalsIgnoreCase(name)){
                    requestTemplate.header(CommonConstant.RECOURCE_KEY, request.getHeader(name));
                }else if(CommonConstant.USERNAME.equalsIgnoreCase(name)){
                    requestTemplate.header(CommonConstant.USERNAME, request.getHeader(name));
                }

            }
        }


    }
}