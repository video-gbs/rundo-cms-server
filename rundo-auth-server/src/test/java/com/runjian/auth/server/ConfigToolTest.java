package com.runjian.auth.server;

import com.runjian.auth.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Test
    public void testBCryptPasswordEncoder() {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("123456");
        System.out.println(password);
    }

    @Test
    public void testJWT() throws Exception {
        String jwt = JwtUtil.createJWT("2123");
        System.out.println(jwt);
        Claims claims = JwtUtil.parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhZDZlZDYwMDQyNjY0ZTc3YjUxNGEwYTJiZDJlOGIxYiIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY3Mjk2ODU1NywiZXhwIjoxNjcyOTcyMTU3fQ.OJr_CH9I9Vxhg8kJzc666X53vZk_GffjjKTX1ZSTfGE");
        String subject = claims.getSubject();
        System.out.println(subject);
    }
}
