package com.runjian.auth.server.domain.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UserInfoDTO
 * @Description 登录参数
 * @date 2023-01-05 周四 17:11
 */
@Data
public class UserInfoDTO {

    @ApiParam("用户名")
    private String username;

    @ApiParam("密码")
    private String password;
}
