package com.runjian.device.expansion.service;

import org.springframework.util.ObjectUtils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeoutExample {
    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger();
        // 创建一个异步任务
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            // 异步执行的代码
            while (true){

                i.getAndIncrement();
            }
        });

        try {
            // 设置超时时间为10秒，并获取结果
            Boolean result = future.get(10, TimeUnit.SECONDS);
            System.out.println("Result: " + result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // 如果在超时时间内没有完成，或者发生异常，则取消任务
            future.cancel(true);

        }
    }
}