package com.runjian.device.expansion.vo.feign;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 云台控制请求体
 * @author Miracle
 * @date 2023/1/9 14:23
 */
@Data
public class PutPtzControlReq {

    /**
     * 通道Id
     */
    @NotNull(message = "通道Id不能为空")
    @Range(min = 1, message = "非法通道Id")
    private Long channelId;

    /**
     * 指令
     */
    @NotNull(message = "指令Code不能为空")
    @Range(min = 0, message = "非法指令Code")
    private Integer commandCode;

    /**
     * 水平速度
     */
    @Range(min = 0, max = 255, message = "速度取值范围在0-255")
    private Integer horizonSpeed;

    /**
     * 垂直速度
     */
    @Range(min = 0, max = 255, message = "速度取值范围在0-255")
    private Integer verticalSpeed;

    /**
     * 缩放速度
     */
    @Range(min = 0, max = 255, message = "速度取值范围在0-255")
    private Integer zoomSpeed;

    /**
     * 总速度
     */
    @Range(min = 0, max = 255, message = "速度取值范围在0-255")
    private Integer totalSpeed;


}
