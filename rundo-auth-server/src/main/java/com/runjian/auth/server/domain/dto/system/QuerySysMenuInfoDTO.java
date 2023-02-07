package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName QuerySysMenuInfoDTO
 * @Description 菜单查询参数
 * @date 2023-02-06 周一 16:45
 */

@Data
@AllArgsConstructor
@ApiModel(value = "菜单查询参数", description = "菜单查询参数")
public class QuerySysMenuInfoDTO {

    @ApiModelProperty(value = "菜单类型",notes = "应用1，配置：2，运维3")
    private Long menuType;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("跳转URL")
    private String url;

}
