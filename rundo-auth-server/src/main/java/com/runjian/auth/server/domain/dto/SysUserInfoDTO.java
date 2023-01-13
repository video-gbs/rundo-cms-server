package com.runjian.auth.server.domain.dto;

import com.runjian.auth.server.entity.system.SysUserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysUserInfoDTO
 * @Description 新建用户
 * @date 2023-01-10 周二 9:30
 */
@Data
@ApiModel(value = "新建用户", description = "用户信息")
public class SysUserInfoDTO {

    private SysUserInfo userInfo;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long orgId;

    /**
     * 角色信息
     */
    @ApiModelProperty("角色编号列表")
    private List<Long> roleIds;

}
