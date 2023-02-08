package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysRoleInfoDTO
 * @Description 新建角色
 * @date 2023-01-10 周二 14:02
 */
@Data
@ApiModel(value = "添加角色", description = "角色信息")
public class AddSysRoleInfoDTO {

    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @ApiModelProperty("角色描述")
    private String roleDesc;

    //////////////////////权限配置//////////////////////
    // 1 系统权限

    // 1-1 应用权限
    @ApiModelProperty("应用菜单列表")
    List<Long> appIds;

    // 1-2 配置菜单列表
    @ApiModelProperty("配置菜单列表")
    List<Long> menuIds;

    // 1-3 配置菜单列表
    @ApiModelProperty("运维菜单列表")
    List<Long> configIds;

    @ApiModelProperty("接口ID列表")
    List<Long> apiIds;

    // 1-4 部门管理权限
    @ApiModelProperty("部门ID列表")
    List<Long> orgIds;

    // 1-5 安防区域权限
    @ApiModelProperty("安防区域ID列表")
    List<Long> areaIds;

    // 2 资源权限

    // 资源权限
    // 视频通道资源
    @ApiModelProperty("视频通道ID列表")
    List<Long> channelIds;
    @ApiModelProperty("通道操作ID列表")
    List<Long> operationIds;
    // 电视墙
    // 。。。。



}
