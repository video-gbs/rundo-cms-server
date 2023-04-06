package com.runjian.device.expansion.vo.feign.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * 云台控制请求体
 * @author Miracle
 * @date 2023/1/9 14:23
 */
@Data
public class FeignPtzControlReq {

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
    private Integer cmdCode;

    /**
     * 通用指令值
     */
    private Integer cmdValue;

    /**
     * 指令值
     */
    @Size(max = 30, message = "指令参数超出合法值")
    private Map<String, Object> valueMap;

}
