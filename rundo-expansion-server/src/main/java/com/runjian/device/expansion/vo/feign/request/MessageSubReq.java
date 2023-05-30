package com.runjian.device.expansion.vo.feign.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 订阅消息请求体
 * @author Miracle
 * @date 2023/5/16 15:19
 */
@Data
public class MessageSubReq {

    /**
     * 服务名称
     */
    @NotBlank(message = "服务名称不能为空")
    private String serviceName;

    /**
     * 消息类型
     */
    @NotNull(message = "消息类型不能为空")
    @Size(min = 1, max = 20, message = "消息类型长度非法")
    private Set<String> msgTypes;
}
