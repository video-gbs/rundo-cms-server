package com.runjian.device.expansion.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("rundo_gis_video_area_config")
public class GisVideoAreaConfig {
  @TableId(type = IdType.AUTO)
  private long id;
  private Long gisConfigId;
  private Long videoAreaId;
  private Double longitude;
  private Double latitude;
  private Double height;

  private double heading;

  private double pitch;

  private double roll;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long deleted;


}
