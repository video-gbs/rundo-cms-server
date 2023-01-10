package com.runjian.device.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 设备注册状态转为成功请求体
 * @author Miracle
 * @date 2023/1/10 10:09
 */
@Data
public class PutDeviceSignSuccess {

    @NotNull(message = "设备ID不能为空")
    @Range(min = 1, message = "非法设备ID")
    private Long deviceId;
}
