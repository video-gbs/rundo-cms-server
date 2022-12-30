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
 * 菜单信息表
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2022-12-30 10:39:49
 */
@Getter
@Setter
@TableName("tb_sys_menu_info")
public class SysMenuInfo extends BaseDomain {

    private static final long serialVersionUID = 1L;

    /**
     * 直接父级菜单ID
     */
    @TableField("menu_pid")
    private Long menuPid;

    /**
     * 所有间接父级菜单ID
     */
    @TableField("menu_pids")
    private String menuPids;

    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 菜单顺序
     */
    @TableField("menu_sort")
    private String menuSort;

    /**
     * 是否子节点
     */
    @TableField("leaf")
    private String leaf;

    /**
     * 跳转URL
     */
    @TableField("url")
    private String url;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 是否隐藏
     */
    @TableField("hidden")
    private String hidden;

    /**
     * 前端组件import路径
     */
    @TableField("view_import")
    private String viewImport;

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
