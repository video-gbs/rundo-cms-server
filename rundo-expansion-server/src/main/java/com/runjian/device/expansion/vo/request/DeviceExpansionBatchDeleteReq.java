package com.runjian.device.expansion.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author chenjialing
 */
@Data
public class DeviceExpansionBatchDeleteReq {

    @ApiModelProperty("编码器id")
    @NotNull(message = "编码器id参数不得为空")
    private List<Integer> idList;
}
