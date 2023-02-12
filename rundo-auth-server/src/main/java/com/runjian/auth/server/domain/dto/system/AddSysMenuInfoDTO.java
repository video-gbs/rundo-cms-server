package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysMenuInfoDTO
 * @Description 添加菜单
 * @date 2023-01-10 周二 10:38
 */@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "添加菜单", description = "菜单信息")
public class AddSysMenuInfoDTO {

    @ApiModelProperty("所属应用ID")
    @NotNull
    private Long appId;

    @ApiModelProperty("上级菜单ID")
    @NotNull
    private Long menuPid;

    @ApiModelProperty("菜单名称")
    @NotNull
    private String menuName;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("排序")
    @NotNull
    private Integer menuSort;

    @ApiModelProperty("跳转URL")
    private String url;

    @ApiModelProperty(value = "前端组件Import路径")
    private String viewImport;

    @ApiModelProperty(value = "禁用状态",notes = "0正常，1禁用")
    private Integer status;

    @ApiModelProperty(value = "隐藏状态",notes = "0正常，1禁用")
    private Integer hidden;
}
