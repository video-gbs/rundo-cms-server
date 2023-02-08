package com.runjian.device.expansion.vo.request;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author chenjialing
 */
@Data
@TableName("rundo_device_channel_expansion")
@ApiModel(value = "设备通道信息表", description = "接口信息表")
public class DeviceChannelExpansionReq {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("编码器id")
    @NotNull(message = "编码器id不能为空")
    @Range(min = 1, message = "非法编码器id")
    private Long deviceExpansionId;

    @ApiModelProperty("通道名称")
    @NotNull(message = "通道名称不能为空")
    private String channelName;


    @ApiModelProperty("通道类型：0视频，1音频，2告警")
    @NotNull(message = "通道类型不能为空")
    @Range(min = -1, message = "非法通道类型id")
    private Integer channelType;


    @ApiModelProperty("外观类型")
    @NotNull(message = "外观类型不能为空")
    @Range(min = -1, message = "非法外观类型id")
    private Integer ptzType;

    @ApiModelProperty("通道编码")
    @NotNull(message = "通道编码不能为空")
    private String channelCode;

    @ApiModelProperty("安防区域id")
    @NotNull(message = "安防区域id不能为空")
    @Range(min = 1, message = "非法安防区域id")
    private Long videoAreaId;


    @ApiModelProperty("通道编码")
    private String gb28181Code;



    @ApiModelProperty("朝向")
    private String faceLocation;


    @ApiModelProperty("安装地点")
    private String instalLocation;

    @ApiModelProperty("高度")
    private double height;

    @ApiModelProperty("创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime installTime;



    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;


}