package com.runjian.auth.server.model.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysAppInfoDTO
 * @Description 添加应用
 * @date 2023-01-10 周二 10:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "添加应用", description = "应用信息")
public class SysAppInfoDTO {

    @ApiModelProperty(value = "应用名称",required = true)
    @NotBlank
    private String appName;

    @ApiModelProperty("应用所在IP")
    private String appIp;

    @ApiModelProperty("应用服务端口")
    private String appPort;

    @ApiModelProperty("应用简介")
    private String appDesc;

    @ApiModelProperty(value = "禁用状态", notes = "0正常，1禁用")
    private Integer status;
}
