package com.runjian.auth.server.model.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UserInfoDTO
 * @Description 登录参数
 * @date 2023-01-05 周四 17:11
 */
@Data
@ApiModel(value = "登录参数", description = "登录参数")
public class UserInfoDTO {

    @ApiModelProperty(value = "用户名", required = true, position = 1)
    private String username;

    @ApiModelProperty(value = "密码", required = true, position = 2)
    private String password;
}
