package com.runjian.device.expansion.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("rundo_gis_video_area_config")
public class GisVideoAreaConfig {

  private long id;
  private long gisConfigId;
  private long videoAreaId;
  private double longitude;
  private double latitude;
  private double height;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private long deleted;


}
