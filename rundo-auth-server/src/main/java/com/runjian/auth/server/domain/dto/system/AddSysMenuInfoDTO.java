package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

    @ApiModelProperty("排序")
    @NotNull
    @Min(value = 0)
    @Max(100)
    private Integer menuSort;

    @ApiModelProperty("跳转URL")
    private String path;

    @ApiModelProperty("前端组件import路径")
    private String component;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("菜单名称")
    private String title;

    @ApiModelProperty(value = "禁用状态",notes = "0正常，1禁用")
    private Integer status;

    @ApiModelProperty(value = "隐藏状态",notes = "0正常，1禁用")
    private Integer hidden;
}
