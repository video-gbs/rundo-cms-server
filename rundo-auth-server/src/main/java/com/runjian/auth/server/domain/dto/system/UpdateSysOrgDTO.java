package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UpdateSysOrgDTO
 * @Description 更新部门
 * @date 2023-01-31 周二 20:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "更新部门", description = "编辑信息")
public class UpdateSysOrgDTO extends AddSysOrgDTO{
    @ApiModelProperty("编号ID")
    private Long id;
}
