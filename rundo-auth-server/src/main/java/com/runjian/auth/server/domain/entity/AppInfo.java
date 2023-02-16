package com.runjian.auth.server.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @ApiModelProperty("前端组件import路径")
    @TableField("component")
    private String component;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AppInfo appInfo = (AppInfo) obj;
        if (!Objects.equals(id, appInfo.id)) {
            return false;
        }

        if (!Objects.equals(appType, appInfo.appType)) {
            return false;
        }
        if (!Objects.equals(appName, appInfo.appName)) {
            return false;
        }
        if (!Objects.equals(appIcon, appInfo.appIcon)) {
            return false;
        }
        if (!Objects.equals(appIp, appInfo.appIp)) {
            return false;
        }
        if (!Objects.equals(appPort, appInfo.appPort)) {
            return false;
        }
        if (!Objects.equals(appUrl, appInfo.appUrl)) {
            return false;
        }
        if (!Objects.equals(component, appInfo.component)) {
            return false;
        }
        if (!Objects.equals(appDesc, appInfo.appDesc)) {
            return false;
        }
        if (!Objects.equals(deleteFlag, appInfo.deleteFlag)) {
            return false;
        }

        if (!Objects.equals(createdBy, appInfo.createdBy)) {
            return false;
        }

        if (!Objects.equals(updatedBy, appInfo.updatedBy)) {
            return false;
        }
        if (!Objects.equals(createdTime, appInfo.createdTime)) {
            return false;
        }

        if (!Objects.equals(updatedTime, appInfo.updatedTime)) {
            return false;
        }
        return true;
    }
}