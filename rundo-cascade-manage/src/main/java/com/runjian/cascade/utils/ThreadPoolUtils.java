package com.runjian.cascade.utils;

import lombok.Data;

import java.util.concurrent.*;

/**
 * @author Miracle
 * @date 2023/12/26 18:04
 */

public class ThreadPoolUtils {

    public static ThreadPoolExecutor workExecutor = new ThreadPoolExecutor(1, 5,
            500L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10));

}
