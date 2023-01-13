package com.runjian.auth.server.model.dto.video;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName VideoAraeDTO
 * @Description 添加安防区域
 * @date 2023-01-13 周五 14:34
 */
@Data
@ApiModel(value = "VideoArae对象", description = "安保区域")
public class VideoAreaDTO {

    @ApiModelProperty("安防区域名称")
    private String areaName;

    @ApiModelProperty("直接上级")
    private Long areaPid;

    @ApiModelProperty("描述信息")
    private String description;
}
