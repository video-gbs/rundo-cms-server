package com.runjian.auth.server.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName UpdateSysMenuInfoDTO
 * @Description 更新菜单
 * @date 2023-01-30 周一 14:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "更新菜单", description = "接口信息")
public class UpdateSysMenuInfoDTO extends AddSysMenuInfoDTO {
    @ApiModelProperty("编号ID")
    private Long id;
}
