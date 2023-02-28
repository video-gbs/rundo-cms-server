package com.runjian.device.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 设备主动注册请求体
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Data
public class PostDeviceSignInReq {

    /**
     * 设备id
     */
    @NotNull(message = "设备id不能为空")
    @Range(min = 1, message = "非法设备id")
    private Long deviceId;

    /**
     * 网关id
     */
    @NotNull(message = "网关id不能为空")
    @Range(min = 1, message = "非法网关id")
    private Long gatewayId;

    /**
     * 数据原始ID
     */
    @NotBlank(message = "原始id不能为空")
    private String originId;

    /**
     * 在线状态
     */
    @NotNull(message = "在线状态不能为空")
    @Range(min = 0, max = 1, message = "非法在线状态")
    private Integer onlineState;

    /**
     * 设备类型 1-设备 2-NVR 3-DVR 4-CVR 5-未知
     */
    @Range(min = 1, max = 5, message = "非法设备类型")
    private Integer deviceType;

    /**
     * ip地址
     */
    @Size(max = 50, message = "非法ip地址")
    private String ip;

    /**
     * 端口
     */
    @Size(max = 10, message = "非法端口号")
    private String port;

    /**
     * 名称
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
     * 固件版本
     */
    private String firmware;

    /**
     * 云台类型
     */
    private Integer ptzType;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
