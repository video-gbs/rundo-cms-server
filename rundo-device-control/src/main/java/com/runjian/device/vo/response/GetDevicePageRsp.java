package com.runjian.device.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/2/6 16:19
 */
@Data
public class GetDevicePageRsp {

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 网关id
     */
    private Long gatewayId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 网关名称
     */
    private String gatewayName;

    /**
     * 注册状态
     */
    private Integer signState;

    /**
     * 在线状态
     */
    private Integer onlineState;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
