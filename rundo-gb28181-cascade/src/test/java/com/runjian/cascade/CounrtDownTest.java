package com.runjian.cascade;

import com.runjian.cascade.gb28181.bean.ParentPlatform;
import com.runjian.cascade.service.IPlatformCommandService;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class CounrtDownTest {
    @Test
    public void testCount() throws InterruptedException {

        ConcurrentHashMap<String, CountDownLatch> locks = IPlatformCommandService.locks;

        String lockKey = "_register";
        locks.putIfAbsent(lockKey, new CountDownLatch(1));
        try {
            Thread thread = new Thread(() -> {
                testNotify();

            });
            thread.start();


        }catch (Exception e){
            throw  new BusinessException(BusinessErrorEnums.CASCADE_REGISTER_ERROR);
        }

        locks.get(lockKey).await(10, TimeUnit.SECONDS);



    }

    private void testNotify(){
        ConcurrentHashMap<String, CountDownLatch> locks = IPlatformCommandService.locks;

        String lockKey = "_register";

        CountDownLatch latch = locks.remove(lockKey);
        if (latch != null) {
//            latch.countDown();
        }
    }
}
