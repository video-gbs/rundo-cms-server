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
public class StreamControlReq {

    /**
     * 调度服务id
     */
    @NotNull(message = "调度服务id不能为空")
    @Range(min = 1, message = "非法调度服务id")
    private Long dispatchId;

    /**
     * 流id
     */
    @NotBlank(message = "流id不能为空")
    @Size(max = 50, message = "非法流id")
    private String streamId;

    /**
     * 数据体
     */
    private Map<String, Object> mapData;
}
