package com.runjian.auth.server.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-04 15:16:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_sys_user_info")
@ApiModel(value = "SysUserInfo对象", description = "用户信息表")
public class SysUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("用户账户")
    @TableField("user_account")
    private String userAccount;

    @ApiModelProperty("用户姓名")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty("用户密码")
    @TableField("password")
    private String password;

    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("工号")
    @TableField("job_no")
    private String jobNo;

    @ApiModelProperty("地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("有效期起")
    @TableField("expiry_date_start")
    private Date expiryDateStart;

    @ApiModelProperty("有效期终")
    @TableField("expiry_date_end")
    private Date expiryDateEnd;

    @ApiModelProperty("租户号")
    @TableField("tenant_id")
    private Long tenantId;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    private String deleteFlag;

    @ApiModelProperty("创建人")
    @TableField("created_by")
    private Long createdBy;

    @ApiModelProperty("更新人")
    @TableField("updated_by")
    private Long updatedBy;

    @ApiModelProperty("创建时间")
    @TableField("created_time")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @TableField("updated_time")
    private Date updatedTime;


}
