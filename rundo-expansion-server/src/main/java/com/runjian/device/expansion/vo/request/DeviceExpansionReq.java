package com.runjian.device.expansion.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author chenjialing
 */
@Data
public class DeviceExpansionReq {
    @ApiModelProperty("设备名称")
    @NotNull(message = "设备名称不能为空")
    private String deviceName;

    @ApiModelProperty("设备类型")
    @NotNull(message = "设备类型不能为空")
    private String deviceType;

    @ApiModelProperty("厂商")
    private String manufacturer;

    @ApiModelProperty("mode")
    private String mode;


    @ApiModelProperty("用户名")
    private String account;


    @ApiModelProperty("密码")
    private String password;


    @ApiModelProperty("国标编码")
    private String gb28181Code;


    @ApiModelProperty("安防区域id")
    @NotNull(message = "安防区域id不能为空")
    @Range(min = 1, message = "非法安防区域id")
    private Long videoAreaId;

    @ApiModelProperty("ip")
    @NotNull(message = "ip不能为空")
    private String ip;

    @ApiModelProperty("端口")
    @Range(min = 1, message = "非法端口")
    @NotNull(message = "端口不得为空")
    private int port;


    @ApiModelProperty("设备网关ID")
    @NotNull(message = "设备网关不得为空")
    private String gatewayId;

    @ApiModelProperty("transport")
    @NotNull(message = "传输协议不得为空")
    private String transport;

    @ApiModelProperty("状态值")
    private int onlineState;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;
}
