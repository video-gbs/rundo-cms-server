package com.runjian.device.constant;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Miracle
 * @date 2023/1/9 18:04
 */
public class Constant {

    public static final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() + 1,
            Runtime.getRuntime().availableProcessors() * 2,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(200),
            r -> {
                Thread t = new Thread(r);
                t.setName("order-thread");
                // 判断是否是守护线程，如果是关闭
                if (t.isDaemon()) {
                    t.setDaemon(false);
                }
                // 判断是否是正常优先级，如果不是调整会正常优先级
                if (Thread.NORM_PRIORITY != t.getPriority()) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
                return t;
            },
            (r, executor) -> System.err.println("拒绝策略" + r)
    );
}
