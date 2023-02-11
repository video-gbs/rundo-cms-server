package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UpdateVideoAreaDTO
 * @Description 更新安防区域
 * @date 2023-02-01 周三 14:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "VideoArea对象", description = "安保区域")
public class UpdateVideoAreaDTO extends AddVideoAreaDTO {
    @ApiModelProperty("主键ID")
    private Long id;
}
