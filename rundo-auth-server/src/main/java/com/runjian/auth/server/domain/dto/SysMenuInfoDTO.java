package com.runjian.auth.server.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class SysMenuInfoDTO {

    @ApiModelProperty("所属应用ID")
    private Long appId;

    @ApiModelProperty("上级菜单ID")
    private Long menuPid;

    @ApiModelProperty("菜单间接父ID")
    private String menuPids;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单图标")
    private String menuIcon;

    @ApiModelProperty("接口排序")
    private String menuSort;

    @ApiModelProperty("跳转链接")
    private String url;

    @ApiModelProperty(value = "前端组件Import路径")
    private String ImportPath;

    @ApiModelProperty(value = "禁用状态",notes = "0正常，1禁用")
    private Integer status;

    @ApiModelProperty(value = "隐藏状态",notes = "0正常，1禁用")
    private Integer hideStatus;
}
