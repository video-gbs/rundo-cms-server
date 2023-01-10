package com.runjian.auth.server.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableField("group_name")
    private String groupName;

    @ApiModelProperty("分组编码")
    @TableField("group_code")
    private String groupCode;

    @ApiModelProperty("字典项名称")
    @TableField("item_name")
    private String itemName;

    @ApiModelProperty("字典值")
    @TableField("item_value")
    private String itemValue;

    @ApiModelProperty("字典描述")
    @TableField("item_desc")
    private String itemDesc;
}
