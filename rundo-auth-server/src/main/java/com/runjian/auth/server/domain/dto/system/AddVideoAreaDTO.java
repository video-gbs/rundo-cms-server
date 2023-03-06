package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName VideoAraeDTO
 * @Description 添加安防区域
 * @date 2023-01-13 周五 14:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "VideoArea对象", description = "安保区域")
public class AddVideoAreaDTO {

    @ApiModelProperty("安防区域名称")
    @Length(min = 1, max = 32, message = "内容过大")
    @NotNull(message = "安防区域名称不能为空")
    @NotBlank(message = "安防区域名称 或者只包含空格")
    @NotEmpty(message = "安防区域名称不能为空")
    private String areaName;

    @ApiModelProperty("直接上级")
    @NotNull(message = "上级区域不能为空")
    private Long areaPid;

    @ApiModelProperty("描述信息")
    @Length(max = 128, message = "描述信息内容过长")
    private String description;
}
