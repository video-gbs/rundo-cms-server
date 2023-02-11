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
 * 应用信息
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-04 15:16:33
 */
@Getter
@Setter
@TableName("app_info")
@ApiModel(value = "AppInfo对象", description = "应用信息")
public class AppInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("应用分类，1功能，2配置，3运维")
    @TableField("app_type")
    private Integer appType;

    @ApiModelProperty("应用名称")
    @TableField("app_name")
    private String appName;

    @ApiModelProperty("应用图标/背景色")
    @TableField("app_icon")
    private String appIcon;

    @ApiModelProperty("应用所在IP")
    @TableField("app_ip")
    private String appIp;

    @ApiModelProperty("应用服务端口")
    @TableField("app_port")
    private Integer appPort;

    @ApiModelProperty("应用跳转路由")
    @TableField("app_url")
    private String appUrl;

    @ApiModelProperty("应用简介")
    @TableField("app_desc")
    private String appDesc;

    @ApiModelProperty(value = "禁用状态", notes = "0正常，1禁用")
    @TableField("status")
    private Integer status;

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
