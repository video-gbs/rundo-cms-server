package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysOrgDTO
 * @Description
 * @date 2023-01-09 周一 18:26
 */
@Data
@ApiModel(value = "新建部门", description = "部门信息")
public class AddSysOrgDTO {

    @ApiModelProperty(value = "上级部门", required = true)
    private Long orgPid;

    @ApiModelProperty(value = "部门名称", required = true)
    private String orgName;

    @ApiModelProperty("部门结构编码")
    private String orgCode;

    @ApiModelProperty("排序")
    private String orgSort;

    @ApiModelProperty(value = "部门负责人")
    private String orgLeader;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "地址")
    private String adders;

    @ApiModelProperty(value = "描述信息")
    private String description;
}
