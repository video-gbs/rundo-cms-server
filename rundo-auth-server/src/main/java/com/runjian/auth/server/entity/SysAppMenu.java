package com.runjian.auth.server.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 应用菜单关联表
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-04 15:16:33
 */
@Getter
@Setter
@TableName("tb_sys_app_menu")
@ApiModel(value = "SysAppMenu对象", description = "应用菜单关联表")
public class SysAppMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;

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

    @ApiModelProperty("应用ID")
    @TableField("app_id")
    private Long appId;

    @ApiModelProperty("菜单ID")
    @TableField("menu_id")
    private String menuId;


}
