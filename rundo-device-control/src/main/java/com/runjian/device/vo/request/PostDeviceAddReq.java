package com.runjian.device.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 设备添加请求体
 * @author Miracle
 * @date 2023/1/9 15:20
 */
@Data
public class PostDeviceAddReq {

    /**
     * 原始设备Id
     */
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;

    /**
     * 网关Id
     */
    @NotNull(message = "网关Id不能为空")
    @Range(min = 1, message = "非法网关Id")
    private Long gatewayId;

    /**
     * 设备类型
     */
    @NotNull(message = "设备类型不能为空")
    @Range(min = 1, max = 5, message = "非法设备类型")
    private Integer deviceType;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 厂商
     */
    private String manufacturer;

    /**
     * 型号
     */
    private String model;

    /**
     * 固件版本号
     */
    private String firmware;

    /**
     * 云台类型
     */
    private Integer ptzType;


}
