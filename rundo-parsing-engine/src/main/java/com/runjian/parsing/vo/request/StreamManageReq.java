package com.runjian.parsing.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * @author Miracle
 * @date 2023/2/10 15:00
 */
@Data
public class StreamManageReq {

    /**
     * 调度服务id
     */
    @NotNull(message = "调度服务id不能为空")
    @Range(min = 1, message = "非法调度服务id")
    private Long dispatchId;

    /**
     * 流id
     */
    @Size(max = 50, message = "非法流id")
    private String streamId;

    /**
     * 消息类型
     */
    @NotBlank(message = "消息类型不能为空")
    @NotNull(message = "消息类型不能为空")
    private String msgType;

    /**
     * 请求超时时间
     */
    private Long outTime = 10L;

    /**
     * 数据体
     */
    private Map<String, Object> dataMap;
}
