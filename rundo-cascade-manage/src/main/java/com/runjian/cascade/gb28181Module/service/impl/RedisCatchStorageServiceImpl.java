package com.runjian.cascade.gb28181Module.service.impl;


import com.runjian.cascade.gb28181Module.common.constant.SipBusinessConstants;
import com.runjian.cascade.gb28181Module.gb28181.bean.PlatformRegisterInfo;
import com.runjian.cascade.gb28181Module.gb28181.bean.SipTransactionInfo;
import com.runjian.cascade.gb28181Module.service.IRedisCatchStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author chenjialing
 */
@Service
@Slf4j
public class RedisCatchStorageServiceImpl implements IRedisCatchStorageService {

    @Autowired
    private RedisTemplate redisTemplate;


    final private String sipCsp = "sipCsp_";



    @Override
    public Long getCSEQ() {
        Long result = redisTemplate.opsForValue().increment(sipCsp, 1L);
        if (result > Long.MAX_VALUE) {
            redisTemplate.opsForValue().set(sipCsp, 1);
            result = 1L;
        }
        return result;
    }

    @Override
    public void updatePlatformRegisterInfo(String callId, PlatformRegisterInfo platformRegisterInfo) {
        String key = SipBusinessConstants.PLATFORM_REGISTER_INFO_PREFIX+":"+callId;
        Duration duration = Duration.ofSeconds(30L);
        redisTemplate.opsForValue().set(key, platformRegisterInfo, duration);
    }

    @Override
    public PlatformRegisterInfo queryPlatformRegisterInfo(String callId) {
        return (PlatformRegisterInfo)redisTemplate.opsForValue().get(SipBusinessConstants.PLATFORM_REGISTER_INFO_PREFIX  + ":" + callId);
    }

    @Override
    public void delPlatformRegisterInfo(String callId) {
        redisTemplate.delete(SipBusinessConstants.PLATFORM_REGISTER_INFO_PREFIX  + ":" + callId);
    }

    @Override
    public void updatePlatformRegisterSip(String platformGbId, SipTransactionInfo sipTransactionInfo) {
        String key = SipBusinessConstants.PLATFORM_REGISTER_SIP_PREFIX   + "_" +  platformGbId;
        redisTemplate.opsForValue().set(key, sipTransactionInfo);
    }

    @Override
    public SipTransactionInfo queryPlatformRegisterSip(String platformGbId) {
        return (SipTransactionInfo)redisTemplate.opsForValue().get(SipBusinessConstants.PLATFORM_REGISTER_SIP_PREFIX  + "_" + platformGbId);
    }

    @Override
    public void delPlatformRegisterSip(String platformGbId) {
        redisTemplate.delete(SipBusinessConstants.PLATFORM_REGISTER_SIP_PREFIX  + "_" + platformGbId);
    }
}
