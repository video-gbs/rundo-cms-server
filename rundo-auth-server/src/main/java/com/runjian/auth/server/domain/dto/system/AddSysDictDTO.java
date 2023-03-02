package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AddSysDictDTO
 * @Description 添加数字字典
 * @date 2023-01-30 周一 14:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSysDictDTO {
    @ApiModelProperty("分组名称")
    @NotNull
    private String groupName;

    @ApiModelProperty("分组编码")
    @NotNull
    private String groupCode;

    @ApiModelProperty("字典项名称")
    @NotNull
    private String itemName;

    @ApiModelProperty("字典值")
    @NotNull
    private String itemValue;

    @ApiModelProperty("字典描述")
    private String itemDesc;
}
