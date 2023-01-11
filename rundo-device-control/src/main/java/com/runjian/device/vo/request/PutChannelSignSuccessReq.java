package com.runjian.device.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 通道注册状态转为成功请求体
 * @author Miracle
 * @date 2023/1/10 10:09
 */
@Data
public class PutChannelSignSuccessReq {

    @NotNull(message = "通道ID不能为空")
    @Range(min = 1, message = "非法通道ID")
    private Long channelId;
}
