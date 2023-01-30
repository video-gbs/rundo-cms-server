package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AddSysDictDTO
 * @Description 添加数字字典
 * @date 2023-01-30 周一 14:58
 */
@Data
public class AddSysDictDTO {
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
