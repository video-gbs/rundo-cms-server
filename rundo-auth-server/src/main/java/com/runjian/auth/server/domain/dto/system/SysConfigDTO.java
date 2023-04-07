package com.runjian.auth.server.domain.dto.system;

import com.runjian.auth.server.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AddSysConfigDTO
 * @Description 添加系统参数配置
 * @date 2023-01-30 周一 14:48
 */
@Data
@AllArgsConstructor
public class SysConfigDTO {

    @ApiModelProperty("编号ID")
    @NotNull(groups = {UpdateGroup.class}, message = "id不能为空")
    private Long id;
}
