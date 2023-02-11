package com.runjian.auth.server.domain.dto.login;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.runjian.auth.server.domain.entity.SysUserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
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
            long flag = LocalDateTimeUtil.between(LocalDateTimeUtil.now(), sysUserInfo.getExpiryDateEnd()).toDays();
            return flag > 0;
        }
        return true;
    }

    /**
     * 账号是否没被锁定
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        // 状态为非0,就是锁定状态
        return sysUserInfo.getStatus().equals(0);
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
