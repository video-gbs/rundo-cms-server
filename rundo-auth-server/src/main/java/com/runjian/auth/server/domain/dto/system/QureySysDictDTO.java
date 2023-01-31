package com.runjian.auth.server.domain.dto.system;

import com.runjian.auth.server.domain.dto.common.CommonPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName QureySysDictDTO
 * @Description 字典分页查询
 * @date 2023-01-31 周二 10:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ApiModel(value = "字典分页查询", description = "应用信息查询条件")
public class QureySysDictDTO extends CommonPage {
    @ApiModelProperty("字典项名称")
    private String itemName;

    @ApiModelProperty("字典值")
    private String itemValue;
}
