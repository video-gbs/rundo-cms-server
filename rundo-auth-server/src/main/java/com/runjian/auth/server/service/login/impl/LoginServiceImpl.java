package com.runjian.auth.server.service.login.impl;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.model.dto.login.LoginUserDTO;
import com.runjian.auth.server.model.dto.login.UserInfoDTO;
import com.runjian.auth.server.service.login.LoginService;
import com.runjian.auth.server.util.JwtUtil;
import com.runjian.auth.server.util.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(UserInfoDTO user) {
        // 调用AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的提示
        // 认证失败，给出对应的提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }

        // 认证通过了，使用userid生成一个JWT令牌 并将JWT存入CommonResponse返回
        LoginUserDTO loginUserDTO = (LoginUserDTO) authenticate.getPrincipal();
        String userid = loginUserDTO.getSysUserInfo().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        map.put("username", loginUserDTO.getSysUserInfo().getUserName());
        map.put("userAccount", loginUserDTO.getSysUserInfo().getUserAccount());
        map.put("jobNo", loginUserDTO.getSysUserInfo().getJobNo());
        map.put("email", loginUserDTO.getSysUserInfo().getEmail());
        map.put("phone", loginUserDTO.getSysUserInfo().getPhone());
        map.put("description", loginUserDTO.getSysUserInfo().getDescription());
        // 把完整的用户信息放入redis中，userId 作为key
        redisCache.setCacheObject("login:" + userid, loginUserDTO);
        return new ResponseResult(200, "登陆成功", map);
    }

    @Override
    public ResponseResult logout() {
        // 获取SecurityContextHolder中的用户ID
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUserDTO loginUserDTO = (LoginUserDTO) authentication.getPrincipal();
        Long userid = loginUserDTO.getSysUserInfo().getId();
        // 删除redis中的UserID
        redisCache.deleteObject("login:" + userid);
        return new ResponseResult(200, "注销成功");
    }

}

