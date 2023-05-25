package com.runjian.parsing.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 通用北向消息传输体
 * @author Miracle
 * @date 2023/1/9 15:11
 */
@Data
public class DeviceControlReq {

    /**
     * 主Id(网关id|设备id|通道id)
     */
    @Range(min = 1, message = "非法主id")
    @NotNull(message = "主ID不能为空")
    private Long mainId;

    /**
     * @see com.runjian.common.constant.IdType
     */
    @NotNull(message = "数据类型不能为空")
    @Range(min = 1, max = 3, message = "非法主id")
    private Integer idType;

    /**
     * 消息类型
     */
    @NotBlank(message = "消息类型不能为空")
    private String msgType;

    /**
     * 过期时间
     */
    private Long outTime = 10L;

    /**
     * 数据集合
     */
    private Map<String, Object> dataMap;


}
