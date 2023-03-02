package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UpdateSysRoleInfoDTO
 * @Description 编辑角色
 * @date 2023-02-02 周四 9:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "编辑角色", description = "编辑角色信息")
public class UpdateSysRoleInfoDTO extends AddSysRoleInfoDTO{

    @ApiModelProperty("编号ID")
    @NotNull
    private Long id;
}
