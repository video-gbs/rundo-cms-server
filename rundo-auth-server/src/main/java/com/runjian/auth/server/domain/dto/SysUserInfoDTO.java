package com.runjian.auth.server.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysUserInfoDTO
 * @Description 新建用户
 * @date 2023-01-10 周二 9:30
 */
@Data
@ApiModel(value = "新建用户", description = "用户信息")
public class SysUserInfoDTO {

    // 基本信息
    @ApiModelProperty(value = "用户账户",required = true)
    private String userAccount;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户密码",required = true)
    private String password;

    @ApiModelProperty("确认密码")
    private String repassword;

    @ApiModelProperty(value = "有效期起",required = true)
    private Date expiryDateStart;

    @ApiModelProperty(value = "有效期终",required = true)
    private Date expiryDateEnd;

    @ApiModelProperty(value = "所属部门",required = true)
    private Long orgId;

    // 详细信息
    @ApiModelProperty("工号")
    private String jobNo;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("角色ID列表")
    List<Long> roleIds;
}
