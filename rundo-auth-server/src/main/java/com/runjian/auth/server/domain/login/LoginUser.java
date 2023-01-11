package com.runjian.auth.server.domain.login;

import cn.hutool.core.date.DateUtil;
import com.runjian.auth.server.entity.system.SysUserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName LoginUserInfo
 * @Description 登录
 * @date 2023-01-04 周三 16:17
 */
public class LoginUser implements UserDetails {

    private SysUserInfo sysUserInfo;

    Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    public SysUserInfo getSysUserInfo() {
        return sysUserInfo;
    }

    public void setSysUserInfo(SysUserInfo sysUserInfo) {
        this.sysUserInfo = sysUserInfo;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
