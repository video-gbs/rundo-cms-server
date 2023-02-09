package com.runjian.device.expansion.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 获取原始待添加的通道
 * @author chenjialing
 */
@Data
@ApiModel(value = "设备通道待发现列表")
public class ChannelExpansionFindlistRsp {

    /**
     * 通道id
     */
    @ApiModelProperty("通道id")
    private Long channelId;

    /**
     * 设备id
     */
    @ApiModelProperty("编码器id")
    private Long deviceExpansionId;
    /**
     * 设备id
     */
    @ApiModelProperty("编码器名称")
    private String deviceExpansionName;

    /**
     * 通道名称
     */
    @ApiModelProperty("通道名称")
    private String channelName;

    /**
     * 原始id
     */
    @ApiModelProperty("原始id")
    private String originId;

    /**
     * 注册状态
     */
    @ApiModelProperty("注册状态")
    private Integer signState;

    /**
     * 在线状态
     */
    @ApiModelProperty("在线状态")
    private Integer onlineState;

    /**
     * ip地址
     */
    @ApiModelProperty("ip地址")
    private String ip;

    /**
     * 端口
     */
    @ApiModelProperty("端口")
    private String port;

    /**
     * 厂商
     */
    @ApiModelProperty("厂商")
    private String manufacturer;

    /**
     * 型号
     */
    @ApiModelProperty("型号")
    private String model;

    /**
     * 固件版本
     */
    @ApiModelProperty("固件版本")
    private String firmware;

    /**
     * 云台类型
     */
    @ApiModelProperty("云台类型")
    private Integer ptzType;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
