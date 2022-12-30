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
 * 组织机构表
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2022-12-30 10:39:49
 */
@Getter
@Setter
@TableName("tb_sys_org")
public class SysOrg extends BaseDomain {

    private static final long serialVersionUID = 1L;

    /**
     * 直接上级组织父级ID
     */
    @TableField("org_pid")
    private Long orgPid;

    /**
     * 所有间接组织父级ID
     */
    @TableField("org_ids")
    private String orgIds;

    /**
     * 组织机构名称
     */
    @TableField("org_name")
    private String orgName;

    /**
     * 组织结构编码
     */
    @TableField("org_code")
    private String orgCode;

    /**
     * 组织机构排序
     */
    @TableField("org_sort")
    private String orgSort;

    /**
     * 地址
     */
    @TableField("adderss")
    private String adderss;

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
     * 层级
     */
    @TableField("level")
    private String level;

    /**
     * 是否叶子节点
     */
    @TableField("leaf")
    private String leaf;

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
