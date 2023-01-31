package com.runjian.auth.server.domain.vo.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysMenuInfoVO
 * @Description 菜单
 * @date 2023-01-11 周三 18:37
 */
@Data
public class SysMenuInfoVO {
    @ApiModelProperty("主键ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;

    @ApiModelProperty("直接父级菜单ID")
    @TableField("menu_pid")
    private Long menuPid;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单顺序")
    private String menuSort;

    @ApiModelProperty("跳转URL")
    private String url;

    @ApiModelProperty("前端组件import路径")
    private String viewImport;

    @ApiModelProperty("禁用状态")
    private Integer status;

    @ApiModelProperty("是否隐藏")
    private Integer hidden;
}
