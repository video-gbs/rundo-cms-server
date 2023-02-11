package com.runjian.auth.server.service.login.impl;

import com.runjian.auth.server.domain.dto.login.LoginUser;
import com.runjian.auth.server.domain.entity.SysUserInfo;
import com.runjian.auth.server.service.login.MyRBACService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UserDetailsServiceImpl
 * @Description 实现 UserDetailsService 接口
 * @date 2023-01-03 周二 14:14
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MyRBACService myRBACService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.加载用户基础信息
        SysUserInfo sysUserInfo = myRBACService.findUserInfoByUserAccount(username);
        // 如果查询不到数据就通过抛出异常来给出提示
        if (Objects.isNull(sysUserInfo)) {
            throw new UsernameNotFoundException("用户账户不能存在");
        }
        // TODO 根据用户ID查询权限信息 添加到LoginUser中
        // 2.加载用户角色列表
        List<String> roleCodes = myRBACService.findRoleInfoByUserAccount(sysUserInfo.getId());
        // 3. 通过用户角色列表加载用户的资源权限列表
        List<String> authorities = new ArrayList<>();
        for (String roleCode : roleCodes) {
            authorities.addAll(myRBACService.findApiUrlByRoleCode(roleCode));
        }
        // 角色是一个特殊的权限，ROLE_前缀 用来满足Spring Security规范
        authorities.addAll(roleCodes);

        // 4.把数据封装为 UserDetails 返回
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUserInfo(sysUserInfo);
        loginUser.setAuthorities(
                AuthorityUtils.commaSeparatedStringToAuthorityList(
                        String.join(",", authorities)
                )
        );
        return loginUser;
    }


}
