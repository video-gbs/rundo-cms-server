package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName MoveSysOrgDTO
 * @Description 移动部门
 * @date 2023-02-01 周三 9:20
 */
@Data
@ApiModel(value = "移动部门", description = "移动部门")
public class MoveSysOrgDTO {

    @ApiModelProperty(value = "部门ID", required = true)
    private Long id;

    @ApiModelProperty(value = "上级部门ID", required = true)
    private Long orgPid;
}
