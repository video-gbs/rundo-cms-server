package com.runjian.auth.server.service.login.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.runjian.auth.server.domain.dto.login.UserInfoDTO;
import com.runjian.auth.server.domain.entity.UserInfo;
import com.runjian.auth.server.mapper.UserInfoMapper;
import com.runjian.auth.server.service.login.LoginService;
import com.runjian.common.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private UserInfoMapper userInfoMapper;

    @Override
    public Map login(UserInfoDTO dto) {
        String userAccount = dto.getUsername();
        String password = dto.getPassword();
        // 从数据库中查取用户
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserAccount, userAccount);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        // 校验用户是否存在
        if (Objects.isNull(userInfo)) {
            throw new BusinessException("用户不存在");
        }
        // 校验密码是否正确
        if (!BCrypt.checkpw(password, userInfo.getPassword())) {
            throw new BusinessException("密码错误");
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
        return map;
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

}

