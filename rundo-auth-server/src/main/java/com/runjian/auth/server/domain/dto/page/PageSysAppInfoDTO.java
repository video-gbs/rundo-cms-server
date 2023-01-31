package com.runjian.auth.server.domain.dto.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName PageSysAppInfoDTO
 * @Description
 * @date 2023-01-31 周二 9:46
 */
@Data
public class PageSysAppInfoDTO extends Page {
    @ApiModelProperty(value = "应用名称", required = true)
    private String appName;

    @ApiModelProperty(value = "应用所在IP", required = true)
    private String appIp;
}
