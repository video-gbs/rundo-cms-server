package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UpdateSysApiInfoDTO
 * @Description 编辑接口
 * @date 2023-01-30 周一 14:22
 */
@Data
@ApiModel(value = "编辑接口", description = "接口信息")
public class UpdateSysApiInfoDTO extends AddSysApiInfoDTO {
    @ApiModelProperty("编号ID")
    private Long id;
}