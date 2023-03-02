package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UpdateSysDictDTO
 * @Description 修改数据字典
 * @date 2023-01-30 周一 15:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "更新数据字典", description = "接口信息")
public class UpdateSysDictDTO extends AddSysDictDTO {
    @ApiModelProperty("主键ID")
    @NotNull
    private Long id;
}
