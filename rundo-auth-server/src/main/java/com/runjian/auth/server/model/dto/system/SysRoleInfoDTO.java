package com.runjian.auth.server.model.dto.system;

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
public class SysRoleInfoDTO {

    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @ApiModelProperty("角色描述")
    private String roleDesc;

    //////////////////////权限配置//////////////////////
    // 系统权限
    // 菜单权限
    // 应用
    @ApiModelProperty("应用ID列表")
    List<Long> appIds;
    // 配置
    @ApiModelProperty("菜单ID列表")
    List<Long> menuIds;
    // 运维
    @ApiModelProperty("接口ID列表")
    List<Long> apiIds;
    // 部门管理权限
    @ApiModelProperty("部门ID列表")
    List<Long> orgIds;

    // 安防区域权限
    @ApiModelProperty("安防区域ID列表")
    List<Long> areaIds;

    // 资源权限
    // 视频通道资源
    @ApiModelProperty("视频通道ID列表")
    List<Long> channelIds;
    @ApiModelProperty("通道操作ID列表")
    List<Long> operationIds;
    // 电视墙
    // 。。。。

}
