package com.runjian.auth.server.entity.system;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 菜单信息表
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-04 15:16:33
 */
@Getter
@Setter
@TableName("tb_sys_menu_info")
@ApiModel(value = "SysMenuInfo对象", description = "菜单信息表")
public class SysMenuInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("直接父级菜单ID")
    @TableField("menu_pid")
    private Long menuPid;

    @ApiModelProperty("所有间接父级菜单ID")
    @TableField("menu_pids")
    private String menuPids;

    @ApiModelProperty("菜单名称")
    @TableField("menu_name")
    private String menuName;

    @ApiModelProperty("菜单顺序")
    @TableField("menu_sort")
    private String menuSort;

    @ApiModelProperty("是否子节点")
    @TableField("leaf")
    private String leaf;

    @ApiModelProperty("跳转URL")
    @TableField("url")
    private String url;

    @ApiModelProperty("图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty("是否隐藏")
    @TableField("hidden")
    private Integer hidden;

    @ApiModelProperty("前端组件import路径")
    @TableField("view_import")
    private String viewImport;

    @ApiModelProperty("禁用状态")
    @TableField("status")
    private Integer status;

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
