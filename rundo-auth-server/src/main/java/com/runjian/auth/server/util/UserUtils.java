package com.runjian.auth.server.util;

import com.runjian.auth.server.domain.dto.login.LoginUser;
import com.runjian.auth.server.domain.entity.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UserUtils
 * @Description 获取用户
 * @date 2023-02-07 周二 20:23
 */
@Component
public class UserUtils {
    public UserInfo getSysUserInfo() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getUserInfo();
    }


}
