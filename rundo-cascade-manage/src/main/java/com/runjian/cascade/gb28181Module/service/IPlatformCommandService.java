package com.runjian.cascade.gb28181Module.service;


import com.runjian.cascade.gb28181Module.gb28181.bean.OtherPlatform;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public interface  IPlatformCommandService{

    public static ConcurrentHashMap<String, CountDownLatch> locks = new ConcurrentHashMap<>();


    /**
     * 注册
     * @param otherPlatform
     */
    Boolean  register(OtherPlatform otherPlatform) throws InterruptedException;

    /**
     * 注册
     * @param otherPlatform
     */
    void  unRegister(OtherPlatform otherPlatform);
}
