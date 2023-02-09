package com.runjian.device.expansion.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author chenjialing
 */
@Data
@TableName("rundo_device_channel_expansion")
@ApiModel(value = "设备通道信息表", description = "接口信息表")
public class DeviceChannelExpansion {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("编码器ID")
    private Long deviceExpansionId;

    @ApiModelProperty("通道名称")
    private String channelName;

    @ApiModelProperty("外观类型")
    private Integer ptzType;

    @ApiModelProperty("状态值")
    private Integer onlineState;


    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("端口")
    private int port;

    @ApiModelProperty("通道编码")
    private String channelCode;

    @ApiModelProperty("通道编码")
    private String gb28181Code;

    @ApiModelProperty("通道类型：0视频，1音频，2告警")
    private Integer channelType;


    @ApiModelProperty("朝向")
    private String faceLocation;


    @ApiModelProperty("安装地点")
    private String instalLocation;

    @ApiModelProperty("高度")
    private double height;

    @ApiModelProperty("创建时间")
    private LocalDateTime installTime;

    @ApiModelProperty("安防区域id")
    private Long videoAreaId;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("编辑时间")
    private Date updatedAt;

    @ApiModelProperty("删除标记")
    private int deleted;








}
