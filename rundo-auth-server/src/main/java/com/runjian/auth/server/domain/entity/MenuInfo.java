package com.runjian.auth.server.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("menu_info")
@ApiModel(value = "MenuInfo对象", description = "菜单信息表")
public class MenuInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("所属应用")
    @TableField("app_id")
    private Long appId;

    @ApiModelProperty("父菜单ID")
    @TableField("menu_pid")
    private Long menuPid;

    @ApiModelProperty("当前菜单所有父菜单")
    @TableField("menu_pids")
    private String menuPids;

    @ApiModelProperty("菜单名称")
    @TableField("menu_name")
    private String menuName;

    @ApiModelProperty("排序")
    @TableField("menu_sort")
    private Integer menuSort;

    @ApiModelProperty("是否子节点")
    @TableField("leaf")
    private Integer leaf;

    @ApiModelProperty("跳转URL")
    @TableField("url")
    private String url;

    @ApiModelProperty("图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty("层级")
    @TableField("level")
    private Integer level;

    @ApiModelProperty("是否隐藏;0:显示(否）,1:隐藏(是)")
    @TableField("hidden")
    private Integer hidden;

    @ApiModelProperty("是否禁用;0:启用(否）,1:禁用(是)")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("前端组件import路径")
    @TableField("view_import")
    private String viewImport;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

    @ApiModelProperty("创建人")
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    @ApiModelProperty("更新人")
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    @ApiModelProperty("创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;


}
