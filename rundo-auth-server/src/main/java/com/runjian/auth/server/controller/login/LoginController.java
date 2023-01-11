package com.runjian.auth.server.controller.login;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.UserInfoDTO;
import com.runjian.auth.server.service.login.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName LoginController
 * @Description 登录登出
 * @date 2023-01-05 周四 17:09
 */
@Api(tags = "登录注销")
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;


    @ApiOperation(value = "登录接口")
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody UserInfoDTO userInfoDTO) {
        return loginService.login(userInfoDTO);
    }

    @ApiOperation(value = "登出接口")
    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }

}