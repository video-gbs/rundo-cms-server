package com.runjian.device.expansion.vo.request;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
public class GisConfigReq {

  private Long id;

  @ApiModelProperty("字典id")
  @Min(value = 1,message = "字典id不得小于1")
  private long dictId;

  @ApiModelProperty("经度")
  @Min(value = 0,message = "经度不得小于0")
  private double longitude;

  @ApiModelProperty("维度")
  @Min(value = 0,message = "维度不得小于0")
  private double latitude;

  @ApiModelProperty("高度")
  @Min(value = 0,message = "高度不得小于0")
  private double height;

  @ApiModelProperty("地图加载地址")
  @Null(message = "地图加载地址不得为null")
  private String url;

}
