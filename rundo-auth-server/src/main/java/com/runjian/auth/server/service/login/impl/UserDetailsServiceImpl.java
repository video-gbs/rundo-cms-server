package com.runjian.auth.server.service.login.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.auth.server.entity.system.SysUserInfo;
import com.runjian.auth.server.mapper.system.*;
import com.runjian.auth.server.mapper.video.ChannelOperationMapper;
import com.runjian.auth.server.mapper.video.VideoAraeMapper;
import com.runjian.auth.server.mapper.video.VideoChannelMapper;
import com.runjian.auth.server.model.dto.login.LoginUser;
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
     * 角色
     */
    @Autowired
    private SysRoleInfoMapper roleInfoMapper;

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
     * 安全区划
     */
    @Autowired
    private VideoAraeMapper videoAraeMapper;

    /**
     * 安全通道
     */
    @Autowired
    private VideoChannelMapper videoChannelMapper;

    /**
     * 通道操作
     */
    @Autowired
    private ChannelOperationMapper channelOperationMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.加载用户信息
        LambdaQueryWrapper<SysUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserInfo::getUserAccount, username);
        SysUserInfo sysUserInfo = sysUserInfoMapper.selectOne(queryWrapper);
        // 如果查询不到数据就通过抛出异常来给出提示
        if (Objects.isNull(sysUserInfo)) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
        // TODO 根据用户ID查询权限信息 添加到LoginUser中
        // 2.加载用户角色列表
        List<String> roleCodes = roleInfoMapper.selectRoleByUserId(sysUserInfo.getId());
        // 3. 通过用户角色列表加载用户的资源权限列表
        List<String> authorities = new ArrayList<>();
        for (String roleCode : roleCodes) {
            // 3.1 应用权限
            // List<String> appInfoList = roleInfoMapper.findAppByRoleCode(roleCode);
            // 3.2 菜单权限
            // List<String> menuInfoList = roleInfoMapper.findMenuByRoleCode(roleCode);
            // 3.3 接口权限
            authorities.addAll(roleInfoMapper.findApiByRoleCode(roleCode));
            // 3.4 安全区划权限

            // 3.5 通道权限

            // 3.6 通道操作权限

        }
        // 角色是一个特殊的权限，ROLE_前缀 用来满足Spring Security规范
        roleCodes = roleCodes.stream()
                .map(rc -> "ROLE_" + rc)
                .collect(Collectors.toList());
        authorities.addAll(roleCodes);

        // 4.把数据封装为 UserDetails 返回
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUserInfo(sysUserInfo);
        loginUser.setAuthorities(
                AuthorityUtils.commaSeparatedStringToAuthorityList(
                        String.join(",", authorities)
                )
        );
        log.info("用户权限信息：{}", JSONUtil.toJsonStr(loginUser));
        return loginUser;
    }


}
