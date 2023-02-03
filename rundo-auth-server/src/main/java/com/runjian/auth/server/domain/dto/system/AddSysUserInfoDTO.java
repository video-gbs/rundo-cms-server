package com.runjian.auth.server.domain.dto.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
public class AddSysUserInfoDTO {

    @ApiModelProperty("用户账户")
    @NotNull
    private String userAccount;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户密码")
    @NotNull
    private String password;

    @ApiModelProperty("确认密码")
    @NotNull
    private String rePassword;

    @ApiModelProperty("有效期起")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryDateStart;

    @ApiModelProperty("有效期终")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryDateEnd;

    @ApiModelProperty("所属部门编号")
    @NotNull
    private Long orgId;

    @ApiModelProperty("工号")
    private String jobNo;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("描述信息")
    private String description;

    @ApiModelProperty("角色编号列表")
    private List<Long> roleIds;

}
