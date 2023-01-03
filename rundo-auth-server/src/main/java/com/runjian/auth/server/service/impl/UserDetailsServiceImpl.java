package com.runjian.auth.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.runjian.auth.server.entity.SysUserInfo;
import com.runjian.auth.server.mapper.SysUserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UserDetailsServiceImpl
 * @Description 实现 UserDetailsService 接口
 * @date 2023-01-03 周二 14:14
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    public UserDetailsServiceImpl() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 用户查询
        QueryWrapper<SysUserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", username);
        SysUserInfo sysUserInfo = this.sysUserInfoMapper.selectOne(wrapper);
        if (sysUserInfo == null){
            throw new UsernameNotFoundException("根据用户名未查询到用户");
        }
        // TODO 精细化处理
        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
        // 返回权限
        return new User(sysUserInfo.getUserAccount(),sysUserInfo.getPassword(),authorityList);
    }
}
