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
 * @ClassName UpdateSysApiInfoDTO
 * @Description 编辑接口
 * @date 2023-01-30 周一 14:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "编辑接口", description = "接口信息")
public class UpdateSysApiInfoDTO extends AddSysApiInfoDTO {
    @ApiModelProperty("编号ID")
    @NotNull
    private Long id;
}
