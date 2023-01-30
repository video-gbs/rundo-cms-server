package com.runjian.auth.server.model.dto.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UpdateSysApiInfoDTO
 * @Description 编辑接口
 * @date 2023-01-30 周一 14:22
 */
@Data
public class UpdateSysApiInfoDTO extends AddSysApiInfoDTO {
    @ApiModelProperty("编号ID")
    private Long id;
}
