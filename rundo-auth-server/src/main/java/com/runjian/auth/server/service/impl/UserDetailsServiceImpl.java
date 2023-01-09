package com.runjian.auth.server.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.auth.server.domain.dto.LoginUser;
import com.runjian.auth.server.entity.SysUserInfo;
import com.runjian.auth.server.mapper.system.SysApiInfoMapper;
import com.runjian.auth.server.mapper.system.SysAppInfoMapper;
import com.runjian.auth.server.mapper.role.SysRoleUserMapper;
import com.runjian.auth.server.mapper.system.SysMenuInfoMapper;
import com.runjian.auth.server.mapper.system.SysUserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        // 1.根据用户账户查询用户信息
        LambdaQueryWrapper<SysUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserInfo::getUserAccount, username);
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectOne(queryWrapper);
        // 如果查询不到数据就通过抛出异常来给出提示
        if (Objects.isNull(sysUserInfo)) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
        // TODO 根据用户查询权限信息 添加到LoginUser中
        // 获取当前用户角色信息
        List<String> roleCodes = roleUserMapper.selectRoleByUserId(sysUserInfo.getId());
        // 为角色标识加上ROLE_前缀（Spring Security规范）
        roleCodes = roleCodes.stream().map(rc -> "ROLE_" + rc).collect(Collectors.toList());
        log.info("当前用户已有角色{}", JSONUtil.toJsonStr(roleCodes));

        List<String> authorities = new ArrayList<>();
        // 通过角色获取用户的 应用权限列表
        List<String> appAuth = new ArrayList<>();
        // 通过角色获取用户的 菜单权限列表
        List<String> menuAuth = new ArrayList<>();
        // 通过角色获取用户的 接口权限列表
        List<String> apiAuth = new ArrayList<>();
        // 通过角色获取用户的 安全区划权限
        List<String> areaAuth = new ArrayList<>();
        // 通过角色获取用户的 通道权限
        List<String> channelAuth = new ArrayList<>();
        // 通过角色获取用户的 通道操作权限
        List<String> channelOperationAuth = new ArrayList<>();

        // 所有权限进行合并
        authorities.addAll(appAuth);
        authorities.addAll(menuAuth);
        authorities.addAll(apiAuth);
        authorities.addAll(channelAuth);
        authorities.addAll(channelOperationAuth);

        // 把数据封装为 UserDetails 返回
        return new LoginUser(sysUserInfo, authorities);
    }
}
