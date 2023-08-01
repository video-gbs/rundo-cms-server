package com.runjian.device.expansion.aspect.process;

import com.runjian.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


/**
 * @author zhangzhanhong
 * @version 1.0.0
 * @ClassName TokenInfoAspect.java
 * @Description 获取当前登录人信息
 * @createTime 2020年08月17日 16:38:00
 */
@Aspect
@Component
@Slf4j
public class FeignResourceRefreshAspect {



    @Pointcut("@annotation(com.runjian.device.expansion.aspect.annotation.FeignResourceRefreshPoint)")
    public void pointCut() {

    }

    @AfterReturning("pointCut()")
    public void around(JoinPoint joinPoint) throws Throwable {
        //执行方法
        Object[] result = joinPoint.getArgs();


        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Parameter[] parameters = signature.getMethod().getParameters();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //获取登录用户信息（使用这个方便开发的时候屏蔽shiro也可以拿到用户信息）
        String resourceKey = request.getHeader(CommonConstant.RECOURCE_KEY);
        if(ObjectUtils.isEmpty(resourceKey)){
            //不需要判断和获取
        }else {
//            RedisCommonUtil.hget()
        }
//        String schoolIds = request.getHeader(DefContants.SCHOOL_IDS);
//        Object loginUserObj = redisUtil.get(CacheConstant.SYS_USERS_CACHE_JWT + ":" + token);
//        if(loginUserObj==null){
//            log.error("请求头token失效:"+ token);
//            throw new AuthenticationException("请求头token失效:"+token);
//        }
//        LoginUser sysUser = (LoginUser) loginUserObj;
//        // 赋值当前过滤的学校ids
//        sysUser.setSchoolIds(schoolIds);
//        // 获得方法注解
//        for (int i = 0; i < parameters.length; i++) {
//            Parameter parameter = parameters[i];
//            Class<?> paramClazz = parameter.getType();
//            //获取类型所对应的参数对象，实际项目中Controller中的接口不会传两个相同的自定义类型的参数，所以此处直接使用findFirst()
//            Object arg = Arrays.stream(args).filter(ar -> paramClazz.isAssignableFrom(ar.getClass())).findFirst().get();
//
//            if(arg instanceof  LoginUser){
//
//                if(sysUser != null) {
//                    BeanUtils.copyProperties(sysUser, arg);
//                }
//            }
//        }

    }

}
