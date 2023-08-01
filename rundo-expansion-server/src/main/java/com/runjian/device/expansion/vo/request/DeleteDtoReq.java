package com.runjian.device.expansion.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author chenjialing
 */
@Data
public class DeleteDtoReq {



    @ApiModelProperty("id")
    @NotNull(message = "id参数不得为空")
    private List<Long> idList;
}
