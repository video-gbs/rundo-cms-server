package com.runjian.device.expansion.entity;

import com.runjian.device.expansion.constant.SignState;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备信息
 * @author Miracle
 * @date 2023/01/06 16:56
 */
@Data
public class DeviceInfo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 网关id
     */
    private Long gatewayId;

    /**
     * 注册状态 0- 已注册 1-待注册 2-待添加 {@link SignState}
     */
    private Integer signState;

    /**
     * 设备类型 1-设备 2-NVR 3-DVR 4-CVR
     */
    private Integer deviceType;

    /**
     * 在线状态 0-离线 1-在线 {@link com.runjian.common.constant.CommonEnum}
     */
    private Integer onlineState;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}