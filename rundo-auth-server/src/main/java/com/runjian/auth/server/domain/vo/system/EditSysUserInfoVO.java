package com.runjian.auth.server.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName EditSysUserInfoVO
 * @Description 编辑前的用户信息
 * @date 2023-02-02 周四 21:24
 */
@Data
public class EditSysUserInfoVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("用户账户")
    private String userAccount;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("确认密码")
    @NotNull
    private String rePassword;

    @ApiModelProperty("有效期起")
    private LocalDateTime expiryDateStart;

    @ApiModelProperty("有效期终")
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
