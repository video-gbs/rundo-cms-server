package com.runjian.device.expansion.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("rundo_gis_config")
public class GisConfig {

  private long id;
  private Long dictId;
  /**
   * 开启与否
   */
  private Long onStatus;
  private Long deleted;
  private Double longitude;
  private Double latitude;
  private Double height;
  private String url;
  private String createdAt;
  private String updatedAt;

}
