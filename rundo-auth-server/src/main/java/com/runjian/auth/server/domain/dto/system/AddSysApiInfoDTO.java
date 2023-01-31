package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysApiInfoDTO
 * @Description 添加接口
 * @date 2023-01-10 周二 10:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "添加接口", description = "接口信息")
public class AddSysApiInfoDTO {
    @ApiModelProperty("应用ID")
    @NotNull
    private Long appId;

    @ApiModelProperty("接口直接父ID")
    @NotNull
    private Long apiPid;

    @ApiModelProperty("接口名称")
    @NotNull
    private String apiName;

    @ApiModelProperty("跳转链接")
    private String url;

    @ApiModelProperty("接口排序")
    @NotNull
    private Integer apiSort;

    @ApiModelProperty(value = "禁用状态",notes = "0正常，1禁用")
    private Integer status;
}
