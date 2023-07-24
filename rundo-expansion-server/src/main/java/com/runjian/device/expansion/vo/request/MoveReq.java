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
public class MoveReq {



    @ApiModelProperty("安防区域的resourceValue")
    @NotNull(message = "安防区域id不能为空")
    private String pResourceValue;

    @ApiModelProperty("id")
    @NotNull(message = "id参数不得为空")
    private List<Long> idList;
}
