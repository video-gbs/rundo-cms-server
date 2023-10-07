package com.runjian.alarm.vo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 修改通道布防状态请求体
 * @author Miracle
 * @date 2023/10/7 11:14
 */
@Data
public class PutChannelDeployReq {

    /**
     * 通道id数组
     */
    @NotNull(message = "通道id数组不能为空")
    @Size(min = 1, max = 999999, message = "通道id数组不能为空")
    private Set<Long> channelIds;

}
