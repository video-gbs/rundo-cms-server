package com.runjian.auth.server.service.login.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import com.runjian.auth.server.domain.dto.login.UserInfoDTO;
import com.runjian.auth.server.domain.entity.UserInfo;
import com.runjian.auth.server.service.login.LoginService;
import com.runjian.auth.server.service.system.UserInfoService;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.util.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName LoginServiceImpl
 * @Description 登录登出业务实现
 * @date 2023-01-05 周四 17:13
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public CommonResponse login(UserInfoDTO dto) {
        String userAccount = dto.getUsername();
        String password = dto.getPassword();
        // 从数据库中查取用户
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserAccount, userAccount);
        UserInfo userInfo = userInfoService.getOne(queryWrapper);
        // 校验用户是否存在
        if (Objects.isNull(userInfo)) {
            return CommonResponse.failure(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "用户不存在");
        }
        // 校验密码是否正确
        if (!BCrypt.checkpw(password, userInfo.getPassword())) {
            return CommonResponse.failure(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "密码错误");
        }
        // 检测账户是否已过期
        if (!isAccountNonExpired(userInfo)) {
            return CommonResponse.failure(BusinessErrorEnums.USER_ACCOUNT_NOT_ENABLED, "账户已过期");
        }
        // 检测账户是否已禁用
        if (isEnabled(userInfo)) {
            return CommonResponse.failure(BusinessErrorEnums.USER_ACCOUNT_NOT_ENABLED, "账户已禁用");
        }
        // 登录
        StpUtil.login(userInfo.getId());
        // 获取token
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String token = tokenInfo.getTokenValue();

        Map<String, String> map = new HashMap<>();
        map.put("id", userInfo.getId().toString());
        map.put("token", token);
        map.put("username", userInfo.getUserName());
        map.put("userAccount", userInfo.getUserAccount());
        map.put("jobNo", userInfo.getJobNo());
        map.put("email", userInfo.getEmail());
        map.put("phone", userInfo.getPhone());
        map.put("description", userInfo.getDescription());
        return CommonResponse.success(map);
    }

    @Override
    public CommonResponse getCode() {
        SpecCaptcha captcha = new SpecCaptcha(130, 48, 5);
        captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        String verCode = captcha.text().toLowerCase();
        String key = "captcha:" + UUID.randomUUID().toString().replace("-", "");
        redisCache.setCacheObject(key, verCode, 30, TimeUnit.MINUTES);
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("image", captcha.toBase64());
        return CommonResponse.success(map);
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    /**
     * 当前账号是否未过期
     *
     * @param userInfo 用户信息
     * @return true 没过期 false 已过期
     */
    private boolean isAccountNonExpired(UserInfo userInfo) {
        if (null != userInfo.getExpiryDateEnd()) {
            long flag = LocalDateTimeUtil.between(LocalDateTimeUtil.now(), userInfo.getExpiryDateEnd()).toDays();
            return flag > 0;
        }
        return true;
    }

    /**
     * 当前账号是否未锁定
     *
     * @param userInfo
     * @return
     */
    private boolean isAccountNonLocked(UserInfo userInfo) {
        // 禁用状态;0:启用(否）,1:禁用(是)
        Integer locked = 1;
        return locked.equals(userInfo.getStatus());
    }

    /**
     * 当前账号是否被允许
     *
     * @param userInfo
     * @return
     */
    private boolean isEnabled(UserInfo userInfo) {
        // 禁用状态;0:启用(否）,1:禁用(是)
        Integer locked = 1;
        return locked.equals(userInfo.getStatus());
    }
}

