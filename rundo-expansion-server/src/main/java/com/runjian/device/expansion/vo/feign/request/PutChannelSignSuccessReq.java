package com.runjian.device.expansion.vo.feign.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 通道注册状态转为成功请求体
 * @author Miracle
 * @date 2023/1/10 10:09
 */
@Data
public class PutChannelSignSuccessReq {

    @NotNull(message = "通道ID数组不能为空")
    @Size(min = 1, message = "通道id不能为空")
    private List<Long> channelIdList;
}
