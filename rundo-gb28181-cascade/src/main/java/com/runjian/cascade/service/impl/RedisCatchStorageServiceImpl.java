package com.runjian.cascade.service.impl;


import com.runjian.cascade.service.IRedisCatchStorageService;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.constant.LogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

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

}
