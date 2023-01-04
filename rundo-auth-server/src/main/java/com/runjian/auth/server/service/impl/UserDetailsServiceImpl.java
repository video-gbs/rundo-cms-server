package com.runjian.auth.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.auth.server.domain.LoginUser;
import com.runjian.auth.server.entity.SysUserInfo;
import com.runjian.auth.server.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    /**
     * 用户
     */
    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    /**
     * 应用
     */
    @Autowired
    private SysAppInfoMapper appInfoMapper;

    /**
     * 菜单
     */
    @Autowired
    private SysMenuInfoMapper menuInfoMapper;

    /**
     * 接口
     */
    @Autowired
    private SysApiInfoMapper apiInfoMapper;

    /**
     * 角色
     */
    @Autowired
    private SysRoleUserMapper roleUserMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("登录的用户账号为{}", username);
        // 查询用户信息
        LambdaQueryWrapper<SysUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserInfo::getUserAccount, username);
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectOne(queryWrapper);
        // 如果没有查询到用户就抛出异常
        if (sysUserInfo == null) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }

        // TODO 查询对应的权限信息

        // 把数据封装为 UserDetails 返回
        return new LoginUser(sysUserInfo);
    }
}
