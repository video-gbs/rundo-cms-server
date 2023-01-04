package com.runjian.auth.server.domain;

import com.runjian.auth.server.entity.SysUserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {

    private SysUserInfo sysUserInfo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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
     * 暂时未用到，直接返回true，表示账户未过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
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
