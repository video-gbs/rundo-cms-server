package com.runjian.auth.server.util;

import cn.dev33.satoken.secure.BCrypt;
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

    public String encode(String password) {
        return BCrypt.hashpw(password);
    }

}
