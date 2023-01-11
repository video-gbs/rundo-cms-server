package com.runjian.auth.server.service.login;

import com.runjian.auth.server.common.ResponseResult;
import com.runjian.auth.server.domain.dto.UserInfoDTO;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName LoginService
 * @Description 登录接口
 * @date 2023-01-05 周四 17:12
 */
public interface LoginService {
    ResponseResult login(UserInfoDTO dto);

    ResponseResult  logout();

}
