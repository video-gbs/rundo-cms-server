package com.runjian.device.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/12/15 15:58
 */
@Data
public class PutPlatformSubscribeReq {

    /**
     * 设备id
     */
    @NotNull(message = "设备id不能为空")
    private Long deviceId;

    /**
     * 是否订阅
     */
    @NotNull(message = "是否订阅不能为空")
    @Range(min = 0, max = 1, message = "是否订阅取值范围为0或1")
    private Integer isSubscribe;
}
