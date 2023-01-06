package com.runjian.auth.server.controller;

import cn.hutool.json.JSONUtil;
import com.runjian.auth.server.domain.dto.UserInfoDTO;
import com.runjian.auth.server.service.LoginService;
import com.runjian.common.config.response.CommonResponse;
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
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public CommonResponse login(@RequestBody UserInfoDTO userInfoDTO) {
        log.info(JSONUtil.toJsonStr(userInfoDTO));
        return loginService.login(userInfoDTO);
    }

}