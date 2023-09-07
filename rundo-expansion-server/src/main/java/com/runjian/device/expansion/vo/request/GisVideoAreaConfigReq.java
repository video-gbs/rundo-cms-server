package com.runjian.device.expansion.vo.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author chenjialing
 */
@Data
public class GisVideoAreaConfigReq {

  private Long id;

  @ApiModelProperty("gis配置参数id")
  @Min(value = 1,message = "gis配置参数id不得小于1")
  private long gisConfigId;


  @ApiModelProperty("安防区域id")
  @Min(value = 1,message = "安防区域id不得小于1")
  private long videoAreaId;


  @ApiModelProperty("经度")
  @Min(value = 0,message = "经度不得小于0")
  private double longitude;

  @ApiModelProperty("维度")
  @Min(value = 0,message = "维度不得小于0")
  private double latitude;

  @ApiModelProperty("高度")
  @Min(value = 0,message = "高度不得小于0")
  private double height;

}
