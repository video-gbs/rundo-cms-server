package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UpdateSysUserInfoDTO
 * @Description 更新用户信息
 * @date 2023-02-02 周四 14:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "更新用户信息", description = "更新用户信息")
public class UpdateSysUserInfoDTO extends AddSysUserInfoDTO{
    @ApiModelProperty("主键ID")
    private Long id;
}
