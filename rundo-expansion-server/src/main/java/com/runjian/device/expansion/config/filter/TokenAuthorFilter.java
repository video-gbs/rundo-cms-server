//package com.runjian.device.expansion.config.filter;
//
//import com.runjian.common.constant.CommonConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.mybatis.logging.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.util.logging.Logger;
//
///**
// * 请求的基本过滤器 预处理请求头
// * @author chenjialing
// */
//@Component
//@WebFilter(urlPatterns = {"/*"}, filterName = "tokenAuthorFilter")
//@Slf4j
//public class TokenAuthorFilter implements Filter {
//
//
//    @Override
//    public void destroy() {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response,
//                         FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse rep = (HttpServletResponse) response;
//
//        HttpSession session = req.getSession();
//        //LOG.info("Origin:{}", req.getHeader("Origin"));
//
//        //设置允许跨域的配置
//        // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
//        rep.setHeader("Access-Control-Allow-Origin", "*");
//        //rep.setHeader("Access-Control-Allow-Origin", "*");
//        rep.setHeader("Access-Control-Expose-Headers", CommonConstant.AUTHORIZATION);
//        // 允许的访问方法
//        rep.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
//        // Access-Control-Max-Age 用于 CORS 相关配置的缓存
//        rep.setHeader("Access-Control-Max-Age", "3600");
//        rep.setHeader("Access-Control-Allow-Headers", CommonConstant.AUTHORIZATION+", Origin, X-Requested-With, Content-Type, Accept");
//        //若要返回cookie、携带seesion等信息则将此项设置我true
//        rep.setHeader("Access-Control-Allow-Credentials", "true");
//        // 把获取的Session返回个前端Cookie
//        //rep.addCookie(new Cookie("JSSESIONID", session.getId()));
//        chain.doFilter(req, rep);
//
//    }
//
//    @Override
//    public void init(FilterConfig arg0) throws ServletException {
//
//    }
//
//}