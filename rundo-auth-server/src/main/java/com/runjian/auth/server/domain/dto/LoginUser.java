package com.runjian.auth.server.domain.dto;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.annotation.JSONField;
import com.runjian.auth.server.entity.SysUserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName LoginUserInfo
 * @Description 登录
 * @date 2023-01-04 周三 16:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {

    private SysUserInfo sysUserInfo;

    /**
     * 权限信息
     */
    private List<String> permissions;

    /**
     * 存储SpringSecurity需要的权限信息集合
     */
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    public LoginUser(SysUserInfo sysUserInfo, List<String> permissions) {
        this.sysUserInfo = sysUserInfo;
        this.permissions = permissions;
    }

    public LoginUser(SysUserInfo sysUserInfo) {
        this.sysUserInfo = sysUserInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // // jwt 鉴权过中,如果已经存在则会直接反,这里是一个优化，一个线程中你只登录的第一次需要
        // if (Objects.nonNull(authorities)) {
        //     return authorities;
        // }
        // // 将permissions中的权限信息转换为GrantedAuthority对象
        // authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        // return authorities;
        return null;
    }


    /**
     * 获取密码
     *
     * @return
     */
    @Override
    public String getPassword() {
        return sysUserInfo.getPassword();
    }

    /**
     * 获取用户账号
     *
     * @return
     */
    @Override
    public String getUsername() {
        return sysUserInfo.getUserAccount();
    }

    /**
     * 用户是否没有过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        if (null != sysUserInfo.getExpiryDateEnd()) {
            int flag = DateUtil.compare(sysUserInfo.getExpiryDateEnd(), DateUtil.date());
            return flag > 0;
        }
        return true;
    }

    /**
     * 账号是否没被锁定
     * 暂时未用到，直接返回true，表示账户未被锁定
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否没过期
     * 暂时未用到，直接返回true，表示账户密码未过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否可用
     * 暂时未用到，直接返回true，表示账户可用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
