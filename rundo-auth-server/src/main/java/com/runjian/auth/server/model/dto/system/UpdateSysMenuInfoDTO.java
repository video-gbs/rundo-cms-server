package com.runjian.auth.server.model.dto.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UpdateSysMenuInfoDTO
 * @Description 更新菜单
 * @date 2023-01-30 周一 14:32
 */
@Data
public class UpdateSysMenuInfoDTO extends AddSysMenuInfoDTO {
    @ApiModelProperty("编号ID")
    private Long id;
}
