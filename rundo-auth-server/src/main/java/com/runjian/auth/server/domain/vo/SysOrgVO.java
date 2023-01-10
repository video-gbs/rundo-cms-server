package com.runjian.auth.server.domain.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysOrgVO
 * @Description
 * @date 2023-01-10 周二 10:02
 */
public class SysOrgVO {
    @ApiModelProperty(value = "部门名称", required = true)
    private String orgName;

    @ApiModelProperty(value = "部门负责人")
    private String personName;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "描述信息")
    private String description;
}
