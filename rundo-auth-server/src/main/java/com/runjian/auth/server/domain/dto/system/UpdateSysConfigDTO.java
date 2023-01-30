package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UpdateSysConfigDTO
 * @Description
 * @date 2023-01-30 周一 14:52
 */
@Data
@ApiModel(value = "更新系统配置", description = "接口信息")
public class UpdateSysConfigDTO extends AddSysConfigDTO {
    @ApiModelProperty("主键ID")
    private Long id;
}
