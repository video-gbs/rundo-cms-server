package com.runjian.auth.server.model.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysDictDTO
 * @Description 添加数字字典
 * @date 2023-01-10 周二 10:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "数据字典", description = "数据字典表")
public class SysDictDTO {
    @ApiModelProperty("分组名称")
    private String groupName;

    @ApiModelProperty("分组编码")
    private String groupCode;

    @ApiModelProperty("字典项名称")
    private String itemName;

    @ApiModelProperty("字典值")
    private String itemValue;

    @ApiModelProperty("字典描述")
    private String itemDesc;
}
