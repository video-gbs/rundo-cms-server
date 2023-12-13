package com.runjian.device.entity;

import com.runjian.common.constant.SignState;
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

    /**
     * 流类型 {@link com.runjian.device.constant.StreamType}
     */
    private Integer streamType;

    /**
     * 是否订阅下级
     */
    private Integer isSubscribe;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
