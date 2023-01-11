package com.runjian.auth.server.expression;

import com.runjian.auth.server.domain.dto.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName RundoExpressionRoot
 * @Description
 * @date 2023-01-11 周三 10:10
 */
@Component("ex")
public class RundoExpressionRoot {
    public boolean hasAuthority(String authority){
        //获取当前用户的权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        //判断用户权限集合中是否存在authority
        return permissions.contains(authority);
    }
}
