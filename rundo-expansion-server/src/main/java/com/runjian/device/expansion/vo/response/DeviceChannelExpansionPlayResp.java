package com.runjian.device.expansion.vo.response;

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
public class DeviceChannelExpansionPlayResp {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;
    /**
     * 通道id
     */
    @ApiModelProperty("通道id")
    private Long channelId;

    @ApiModelProperty("通道名称")
    private String channelName;

    @ApiModelProperty("外观类型")
    private Integer ptzType;

    @ApiModelProperty("状态值")
    private Integer onlineState;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("端口")
    private Integer port;

    @ApiModelProperty("通道编码")
    private String channelCode;

    @ApiModelProperty("通道编码")
    private String gb28181Code;


    @ApiModelProperty("设备厂商")
    private String manufacturer;

    @ApiModelProperty("通道类型：0视频，1音频，2告警")
    private Integer channelType;


    @ApiModelProperty("朝向")
    private String faceLocation;


    @ApiModelProperty("安装地点")
    private String installLocation;

    @ApiModelProperty("高度")
    private Double height;

    @ApiModelProperty("创建时间")
    private LocalDateTime installTime;


    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;







}
