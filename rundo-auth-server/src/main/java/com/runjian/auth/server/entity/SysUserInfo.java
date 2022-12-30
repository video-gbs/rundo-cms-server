package com.runjian.auth.server.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.runjian.common.base.BaseDomain;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2022-12-30 10:39:49
 */
@Getter
@Setter
@TableName("tb_sys_user_info")
public class SysUserInfo extends BaseDomain {

    private static final long serialVersionUID = 1L;

    /**
     * 用户账户
     */
    @TableField("user_account")
    private String userAccount;

    /**
     * 用户姓名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户密码
     */
    @TableField("password")
    private String password;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 工号
     */
    @TableField("job_no")
    private String jobNo;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 有效期起
     */
    @TableField("expiry_date_start")
    private Date expiryDateStart;

    /**
     * 有效期终
     */
    @TableField("expiry_date_end")
    private Date expiryDateEnd;

    /**
     * 租户号
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 逻辑删除
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    private String deleteFlag;

    /**
     * 创建人
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;


}
