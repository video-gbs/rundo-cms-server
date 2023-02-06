package com.runjian.device.expansion.vo.request.feign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author chenjialing
 */
@Data
public class DeviceReq {

    @ApiModelProperty("国标编码")
    private String deviceId;

    @ApiModelProperty("设备网关ID")
    @NotNull(message = "设备网关不得为空")
    private long gatewayId;


    @ApiModelProperty("设备类型")
    @NotNull(message = "设备类型不能为空")
    private int deviceType;


    @ApiModelProperty("ip")
    @NotNull(message = "ip不能为空")
    private String ip;

    @ApiModelProperty("端口")
    @Range(min = 1, message = "非法端口")
    @NotNull(message = "端口不得为空")
    private int port;


    @ApiModelProperty("设备名称")
    @NotNull(message = "设备名称不能为空")
    private String name;

    @ApiModelProperty("厂商")
    private String manufacturer;

    @ApiModelProperty("model")
    private String model;


    @ApiModelProperty("用户名")
    private String username;


    @ApiModelProperty("密码")
    private String password;



    @ApiModelProperty("transport")
    @NotNull(message = "传输协议不得为空")
    private String transport;


}
