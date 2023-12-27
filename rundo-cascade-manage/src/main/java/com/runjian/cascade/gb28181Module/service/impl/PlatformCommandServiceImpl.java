package com.runjian.cascade.gb28181Module.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.runjian.cascade.conf.SipConfig;
import com.runjian.cascade.gb28181Module.common.constant.SipBusinessConstants;
import com.runjian.cascade.gb28181Module.gb28181.bean.OtherPlatform;
import com.runjian.cascade.gb28181Module.gb28181.bean.ParentPlatform;
import com.runjian.cascade.gb28181Module.gb28181.bean.SipTransactionInfo;
import com.runjian.cascade.gb28181Module.gb28181.conf.DynamicTask;
import com.runjian.cascade.gb28181Module.gb28181.transmit.cmd.ISIPCommanderForPlatform;
import com.runjian.cascade.gb28181Module.service.IPlatformCommandService;
import com.runjian.cascade.gb28181Module.service.IRedisCatchStorageService;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class PlatformCommandServiceImpl implements IPlatformCommandService {

    @Autowired
    ISIPCommanderForPlatform isipCommanderForPlatform;

    @Autowired
    SipConfig sipConfig;

    @Autowired
    private DynamicTask dynamicTask;

    @Autowired
    private IRedisCatchStorageService redisCatchStorageService;

    private ParentPlatform convertPlatformSip(OtherPlatform otherPlatform){
        ParentPlatform parentPlatform = new ParentPlatform();
        BeanUtil.copyProperties(otherPlatform,parentPlatform);
        parentPlatform.setExpires(sipConfig.getExpires());
        parentPlatform.setCharacterSet(sipConfig.getCharacterSet());
        sipConfig.completePlatfrom(parentPlatform);
        return parentPlatform;

    }

    @Override
    public Boolean register(OtherPlatform otherPlatform) {
        ParentPlatform parentPlatform = convertPlatformSip(otherPlatform);
        AtomicReference<Boolean> flag = new AtomicReference<>(true);
        String serverGbId = parentPlatform.getServerGbId();
        String lockKey = serverGbId+SipBusinessConstants.PLATFORM_REGISTER_LOCK_PREFIX;
        locks.putIfAbsent(lockKey, new CountDownLatch(1));
        try {

            isipCommanderForPlatform.register(parentPlatform,(eventResult)->{
                flag.set(false);
                CountDownLatch latch = locks.remove(lockKey);
                if (latch != null) {
                    latch.countDown();
                }
            },null);

        }catch (Exception e){
            throw  new BusinessException(BusinessErrorEnums.CASCADE_REGISTER_ERROR);
        }
        try {
            locks.get(lockKey).await(10,TimeUnit.SECONDS);

        }catch (InterruptedException e){
            e.printStackTrace();

        }
        return flag.get();
    }



    @Override
    public Boolean unRegister(OtherPlatform otherPlatform) {
        ParentPlatform parentPlatform = convertPlatformSip(otherPlatform);
        AtomicReference<Boolean> flag = new AtomicReference<>(true);

        String lockKey = parentPlatform.getServerGbId()+SipBusinessConstants.PLATFORM_UNREGISTER_LOCK_PREFIX;
        locks.putIfAbsent(lockKey, new CountDownLatch(1));
        try {
            SipTransactionInfo sipTransactionInfo = redisCatchStorageService.queryPlatformRegisterSip(parentPlatform.getServerGbId());
            isipCommanderForPlatform.unregister(parentPlatform, sipTransactionInfo, (eventResult)->{
                flag.set(false);
                CountDownLatch latch = locks.remove(lockKey);
                if (latch != null) {
                    latch.countDown();
                }
            }, eventResult -> {
                log.info("[国标级联] 注销成功， 平台：{}", parentPlatform.getServerGbId());
            });

        }catch (Exception e){
            throw  new BusinessException(BusinessErrorEnums.CASCADE_UNREGISTER_ERROR);
        }
        try {
            locks.get(lockKey).await(10,TimeUnit.SECONDS);

        }catch (InterruptedException e){
            e.printStackTrace();

        }
        return flag.get();
    }
}
