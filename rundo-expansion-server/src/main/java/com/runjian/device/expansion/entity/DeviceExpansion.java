package com.runjian.device.expansion.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author chenjialing
 */
@Data
@TableName("rundo_device_expansion")
@ApiModel(value = "设备信息表", description = "接口信息表")
public class DeviceExpansion {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("控制服务id")
    private Long deviceId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty("厂商")
    private String manufacturer;

    @ApiModelProperty("设备型号")
    private String mode;


    @ApiModelProperty("用户名")
    private String account;


    @ApiModelProperty("密码")
    private String password;


    @ApiModelProperty("国标编码")
    private String gb28181Code;


    @ApiModelProperty("安防区域id")
    private Long videoAreaId;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("端口")
    private int port;


    @ApiModelProperty("设备网关ID")
    private String gatewayId;

    @ApiModelProperty("传输协议")
    private String transport;

    @ApiModelProperty("状态值")
    private int onlineState;

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
