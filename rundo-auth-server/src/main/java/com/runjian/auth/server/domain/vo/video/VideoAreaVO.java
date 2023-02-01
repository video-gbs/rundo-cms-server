package com.runjian.auth.server.domain.vo.video;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName VideoAreaVO
 * @Description 安防区域响应实体
 * @date 2023-02-01 周三 14:07
 */
@Data
@ApiModel(value = "VideoArea对象", description = "安保区域")
public class VideoAreaVO {
    @ApiModelProperty("主键ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;
    @ApiModelProperty("安防区域名称")
    private String areaName;

    @ApiModelProperty("直接上级")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long areaPid;

    @ApiModelProperty("描述信息")
    private String description;

    @ApiModelProperty("层级")
    private String level;
}
