package com.runjian.device.expansion.aspect.process;

import com.runjian.common.constant.MarkConstant;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.utils.RedisCommonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * @author chenjialing
 */
@Component
@Aspect
public class ChannelStatusAspect {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IDeviceExpansionService deviceExpansionService;

    @Pointcut("@annotation(com.runjian.device.expansion.aspect.annotation.ChannelStatusPoint)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object arount(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取redis缓存中的数据 然后进行数据的更新
        boolean b = RedisCommonUtil.hasKey(redisTemplate, MarkConstant.REDIS_CHANNEL_ONLINE_STATE);
        if(b){
            //进行redis清空和数据库数据状态更新
            deviceExpansionService.syncDeviceStatus();
        }
        return joinPoint.proceed();

    }
}
