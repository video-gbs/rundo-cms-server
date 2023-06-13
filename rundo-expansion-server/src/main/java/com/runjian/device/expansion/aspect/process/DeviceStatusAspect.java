package com.runjian.device.expansion.aspect.process;

import com.runjian.common.constant.MarkConstant;
import com.runjian.device.expansion.config.MessageSubMapConf;
import com.runjian.device.expansion.constant.SubMsgType;
import com.runjian.device.expansion.service.IDeviceExpansionService;
import com.runjian.device.expansion.utils.RedisCommonUtil;
import com.runjian.device.expansion.vo.feign.response.MessageSubRsp;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;


/**
 * @author chenjialing
 */
@Component
@Aspect
public class DeviceStatusAspect {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IDeviceExpansionService deviceExpansionService;

    @Autowired
    MessageSubMapConf messageSubMapConf;

    @Pointcut("@annotation(com.runjian.device.expansion.aspect.annotation.DeviceStatusPoint)")
    public void pointCut() {

    }


    @Around("pointCut()")
    public Object arount(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取redis缓存中的数据 然后进行数据的更新
        HashMap<String, MessageSubRsp> messageSubRspMap = messageSubMapConf.getMessageSubRspMap();
        if(!ObjectUtils.isEmpty(messageSubRspMap)){
            String msgHandle = messageSubRspMap.get(SubMsgType.DEVICE_ONLINE_STATE.getName()).getMsgHandle();
            String msgLock = messageSubRspMap.get(SubMsgType.DEVICE_ONLINE_STATE.getName()).getMsgLock();
            boolean b = RedisCommonUtil.hasKey(redisTemplate, msgHandle);
            if(b){
                //进行redis清空和数据库数据状态更新
                deviceExpansionService.syncDeviceStatus(msgHandle,msgLock);
            }
        }

        return joinPoint.proceed();

    }
}
