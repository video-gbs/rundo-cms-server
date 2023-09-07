package com.runjian.device.expansion.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("rundo_gis_config")
public class GisConfig {

  private long id;
  private long dictId;
  /**
   * 开启与否
   */
  private long onStatus;
  private long deleted;
  private double longitude;
  private double latitude;
  private double height;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
