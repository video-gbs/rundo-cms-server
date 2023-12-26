package com.runjian.cascade.gb28181Module.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.runjian.cascade.conf.SipConfig;
import com.runjian.cascade.gb28181Module.gb28181.bean.OtherPlatform;
import com.runjian.cascade.gb28181Module.gb28181.bean.ParentPlatform;
import com.runjian.cascade.gb28181Module.gb28181.transmit.cmd.ISIPCommanderForPlatform;
import com.runjian.cascade.gb28181Module.service.IPlatformCommandService;
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


    private ParentPlatform convertPlatformSip(OtherPlatform otherPlatform){
        ParentPlatform parentPlatform = new ParentPlatform();
        BeanUtil.copyProperties(otherPlatform,parentPlatform);
        parentPlatform.setExpires(sipConfig.getExpires());
        parentPlatform.setCharacterSet(sipConfig.getCharacterSet());
        return parentPlatform;

    }

    @Override
    public Boolean register(OtherPlatform otherPlatform) throws InterruptedException {
        ParentPlatform parentPlatform = convertPlatformSip(otherPlatform);
        AtomicReference<Boolean> flag = new AtomicReference<>(true);
        String serverGbId = parentPlatform.getServerGbId();
        String lockKey = serverGbId+"_register";
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

        locks.get(lockKey).await(10,TimeUnit.SECONDS);
        Boolean b = flag.get();
        if(b){
            //成功 设置缓存 启动定时注册周期  以及进行定时心跳发送
            
        }else {
            //异步发送时的失败，或则异常的流程 不处理
        }

        return b;
    }


    @Override
    public Boolean unRegister(OtherPlatform otherPlatform) {
        ParentPlatform parentPlatform = convertPlatformSip(otherPlatform);

        try {


        }catch (Exception e){
            throw  new BusinessException(BusinessErrorEnums.CASCADE_UNREGISTER_ERROR);
        }
    }
}
