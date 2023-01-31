package com.runjian.auth.server.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysAppInfoVO
 * @Description 应用详情
 * @date 2023-01-30 周一 14:12
 */
@Data
public class SysAppInfoVO {

    @ApiModelProperty("主键ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;

    @ApiModelProperty(value = "应用名称")
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
