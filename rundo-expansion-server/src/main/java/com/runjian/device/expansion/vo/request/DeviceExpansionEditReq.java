package com.runjian.device.expansion.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author chenjialing
 */
@Data
public class DeviceExpansionEditReq {

    @ApiModelProperty("设备id")
    @NotNull(message = "设备id不能为空")
    private Long id;

    @ApiModelProperty("设备名称")
    @NotNull(message = "设备名称不能为空")
    private String name;

    @ApiModelProperty("设备类型")
    @NotNull(message = "设备类型不能为空")
    private Integer deviceType;

    @ApiModelProperty("厂商")
    private String manufacturer;

    @ApiModelProperty("mode")
    private String model;


    @ApiModelProperty("用户名")
    private String username;


    @ApiModelProperty("密码")
    private String password;


    @ApiModelProperty("国标编码")
    private String deviceId;


    @ApiModelProperty("安防区域id")
    @NotNull(message = "安防区域id不能为空")
    @Range(min = 1, message = "非法安防区域id")
    private Long videoAreaId;

    @NotNull(message = "安防区域id的目标value不能为空")
    @JsonProperty(value = "pResourceValue")
    private String pResourceValue;

    @ApiModelProperty("ip")
    @NotNull(message = "ip不能为空")
    private String ip;

    @ApiModelProperty("端口")
    @Range(min = 1, message = "非法端口")
    @NotNull(message = "端口不得为空")
    private int port;


    @ApiModelProperty("设备网关ID")
    @NotNull(message = "设备网关不得为空")
    private long gatewayId;

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
