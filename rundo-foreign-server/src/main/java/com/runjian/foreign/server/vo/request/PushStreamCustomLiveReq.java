package com.runjian.foreign.server.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Miracle
 * @date 2023/4/25 11:47
 */
@Data
public class PushStreamCustomLiveReq{

    /**
     * 识别编码
     */
    @Range(min = 1, message = "非法设备识别编码")
    private Long code;

    /**
     * 流媒体服务Id
     */
    @NotNull(message = "流媒体服务id不能为空")
    @Range(min = 1, message = "非法流媒体id")
    private Long dispatchId;

    /**
     * 协议
     */
    @NotBlank(message = "流传输协议不能为空")
    private String protocol;





}
