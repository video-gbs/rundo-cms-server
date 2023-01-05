package com.runjian.auth.server;

import org.junit.jupiter.api.Test;

import static com.alibaba.druid.filter.config.ConfigTools.encrypt;
import static com.alibaba.druid.filter.config.ConfigTools.genKeyPair;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName ConfigToolTest
 * @Description 配置文件密码加密测试类
 * @date 2023-01-03 周二 14:49
 */
public class ConfigToolTest {
    @Test
    public void testPassword() throws Exception {
        String password = "JiangYu)1234";
        String[] arr = genKeyPair(512);
        System.out.println("privateKey:" + arr[0]);
        System.out.println("publicKey:" + arr[1]);
        System.out.println("password:" + encrypt(arr[0], password));
    }
}