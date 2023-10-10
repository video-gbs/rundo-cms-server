package com.runjian.device.expansion.vo.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class GisConfigStatusReq {

  @NotNull(message = "id不得为空")
  private Long id;

  @ApiModelProperty("字典id")
  @Range(min = 0,max = 1,message = "状态值为0，1范围")
  private long onStatus;


}
