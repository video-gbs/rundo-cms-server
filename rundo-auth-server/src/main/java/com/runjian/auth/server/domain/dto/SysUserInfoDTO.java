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

    @ApiModelProperty("用户账户")
    private String userAccount;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("工号")
    private String jobNo;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("有效期起")
    private Date expiryDateStart;

    @ApiModelProperty("有效期终")
    private Date expiryDateEnd;

    @ApiModelProperty("描述信息")
    private String description;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long orgId;

    /**
     * 角色信息
     */
    @ApiModelProperty("角色编号列表")
    private List<Long> roleIds;

}
