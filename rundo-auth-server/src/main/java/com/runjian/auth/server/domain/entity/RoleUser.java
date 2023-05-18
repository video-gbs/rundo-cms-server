package com.runjian.auth.server.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色组织关联表
 */
@Data
@EqualsAndHashCode
@TableName("role_user")
@ApiModel(value = "角色用户关联表", description = "角色用户")
public class RoleUser {
    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Long roleId;

    /**
     * 组织ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

}