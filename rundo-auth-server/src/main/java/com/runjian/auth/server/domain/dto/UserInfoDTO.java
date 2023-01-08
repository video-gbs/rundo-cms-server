package com.runjian.auth.server.domain.dto;

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
@ApiModel(value = "登录参数",description = "登录参数")
@Data
public class UserInfoDTO {

    @ApiModelProperty(value = "用户名",required = true)
    private String username;

    @ApiModelProperty(value = "密码",required = true)
    private String password;
}
