package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysOrgDTO
 * @Description
 * @date 2023-01-09 周一 18:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "新建部门", description = "部门信息")
public class AddSysOrgDTO {

    @ApiModelProperty(value = "上级部门", required = true)
    @NotNull(message = "上级部门")
    private Long orgPid;

    @ApiModelProperty(value = "部门名称", required = true)
    @NotBlank(message = "部门名称")
    @NotNull(message = "部门名称名称不能为空")
    @NotBlank(message = "部门名称名称 或者只包含空格")
    @NotEmpty(message = "部门名称名称不能为空")
    @Length(max = 32 ,message = "部门名称,最长为32字符")
    private String orgName;

    @ApiModelProperty("部门结构编码")
    private String orgCode;

    @ApiModelProperty("排序")
    private Integer orgSort;

    @ApiModelProperty(value = "部门负责人")
    @Length(max = 32,message = "部门负责人名称,最长为32字符")
    private String orgLeader;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @Email
    private String email;

    @ApiModelProperty(value = "地址")
    private String adders;

    @ApiModelProperty(value = "描述信息")
    @Length(max = 128,message = "描述信息，最大长度128个字符")
    private String description;
}
