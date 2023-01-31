package com.runjian.auth.server.domain.dto.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName CommonPage
 * @Description 分页参数
 * @date 2023-01-31 周二 9:42
 */
@Data
public class CommonPage {
    @ApiModelProperty(value = "页面容量")
    private long pageSize;

    @ApiModelProperty("页面大小")
    private long current;

}
