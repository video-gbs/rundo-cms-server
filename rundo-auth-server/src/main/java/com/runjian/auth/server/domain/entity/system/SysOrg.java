package com.runjian.auth.server.domain.entity.system;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 组织机构表
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-04 15:16:33
 */
@Getter
@Setter
@TableName("sys_org")
@ApiModel(value = "SysOrg对象", description = "组织机构表")
public class SysOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("直接上级组织父级ID")
    @TableField("org_pid")
    private Long orgPid;

    @ApiModelProperty("所有间接组织父级ID")
    @TableField("org_pids")
    private String orgPids;

    @ApiModelProperty("组织机构名称")
    @TableField("org_name")
    private String orgName;

    @ApiModelProperty("组织结构编码")
    @TableField("org_code")
    private String orgCode;

    @ApiModelProperty("组织机构排序")
    @TableField("org_sort")
    private Integer orgSort;

    @ApiModelProperty("地址")
    @TableField("adders")
    private String adders;

    @ApiModelProperty("部门负责人")
    @TableField("org_leader")
    private String orgLeader;

    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("层级")
    @TableField("level")
    private Integer level;

    @ApiModelProperty("是否叶子节点")
    @TableField("leaf")
    private Integer leaf;

    @ApiModelProperty("描述信息")
    @TableField("description")
    private String description;

    @ApiModelProperty("禁用状态;0:启用(否）,1:禁用(是)")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("租户号")
    @TableField("tenant_id")
    private Long tenantId;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

    @ApiModelProperty("创建人")
    @TableField(value ="created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    @ApiModelProperty("更新人")
    @TableField(value ="updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    @ApiModelProperty("创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;


}
