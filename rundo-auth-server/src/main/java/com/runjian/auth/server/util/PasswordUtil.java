package com.runjian.auth.server.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName PasswordUtil
 * @Description 密码加密器
 * @date 2023-01-10 周二 11:46
 */
@Component
public class PasswordUtil {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

}
