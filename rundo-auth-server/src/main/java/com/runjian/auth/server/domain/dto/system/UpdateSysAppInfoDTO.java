package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UpdateSysAppInfoDTO
 * @Description 更新应用
 * @date 2023-01-30 周一 14:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "更新应用", description = "接口信息")
public class UpdateSysAppInfoDTO extends AddSysAppInfoDTO {
    @ApiModelProperty("主键ID")
    private Long id;
}
