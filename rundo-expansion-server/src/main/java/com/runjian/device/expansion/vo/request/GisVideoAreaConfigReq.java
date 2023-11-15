package com.runjian.device.expansion.vo.request;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author chenjialing
 */
@Data
public class GisVideoAreaConfigReq {
  @TableId(type= IdType.AUTO)
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
  @NotNull(message = "高度不得为null")
  private double height;

  @ApiModelProperty("偏航角")
  @NotNull(message = "偏航角不得为null")
  private double heading;

  @ApiModelProperty("俯仰角")
  @NotNull(message = "偏航角不得为null")
  private double pitch;

  @ApiModelProperty("翻滚角")
  @NotNull(message = "偏航角不得为null")
  private double roll;


}
