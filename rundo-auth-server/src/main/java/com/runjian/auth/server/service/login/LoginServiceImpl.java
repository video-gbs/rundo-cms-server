package com.runjian.auth.server.service.login;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.LoginUser;
import com.runjian.auth.server.domain.dto.UserInfoDTO;
import com.runjian.auth.server.mapper.role.SysRoleUserMapper;
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
    private SysRoleUserMapper roleUserMapper;


    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(UserInfoDTO dto) {
        // 3.调用AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 认证失败，给出对应的提示
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("用户名或者密码错误");
        }

        log.info("用户登录成功：{}", authentication);
        // 4.如果认证通过了，使用userid生成一个JWT令牌 并将JWT存入CommonResponse返回
        LoginUser loginUser = (LoginUser) (authentication.getPrincipal());
        String userid = loginUser.getSysUserInfo().getId().toString();
        String jwt = JwtUtil.createJWT(userid);

        // 6.获取当前用户的角色信息
        // List<String> roleLists = roleUserMapper.selectRoleByUserId(Long.valueOf(userid));
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);

        // 5.把完整的用户信息放入redis中，userId 作为key
        redisCache.setCacheObject("login:" + userid, loginUser);

        // map.put("role", String.join(",", roleLists));
        return new ResponseResult(200, "登陆成功", map);
    }

    @Override
    public ResponseResult logout() {
        // 获取SecurityContextHolder中的用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getSysUserInfo().getId();
        // 删除redis中的UserID
        redisCache.deleteObject("login:" + userid);
        return new ResponseResult(200, "注销成功");
    }

}

