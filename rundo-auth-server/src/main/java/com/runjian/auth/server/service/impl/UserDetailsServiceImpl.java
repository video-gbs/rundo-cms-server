package com.runjian.auth.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.auth.server.domain.dto.LoginUser;
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
        // 根据用户账户查询用户信息
        LambdaQueryWrapper<SysUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserInfo::getUserAccount, username);
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectOne(queryWrapper);
        // 如果查询不到数据就通过抛出异常来给出提示
        if (sysUserInfo == null) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
        // TODO 根据用户查询权限信息 添加到LoginUser中
        // 获取当前用户角色信息
        // List<String> list = roleUserMapper.selectRoleByUserId(sysUserInfo.getId());
        // log.info("用户的角色为：{}", list);

        // 把数据封装为 UserDetails 返回
        return new LoginUser(sysUserInfo);
    }
}
