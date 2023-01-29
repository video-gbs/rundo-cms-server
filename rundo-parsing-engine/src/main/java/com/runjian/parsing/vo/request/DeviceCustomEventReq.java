package com.runjian.parsing.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotBlank;
import java.util.Map;


/**
 * @author Miracle
 * @date 2023/1/29 15:22
 */
@Data
public class DeviceCustomEventReq {

    /**
     * 设备ID
     */
    @Range(min = 1, message = "设备ID不能为空")
    private Long deviceId;

    /**
     * 网关ID
     */
    @Range(min = 1, message = "非法网关Id")
    private Long gatewayId;

    /**
     * 通道id
     */
    @Range(min = 1, message = "非法通道id")
    private Long channelId;

    /**
     * 消息类型不能为空
     */
    @NotBlank(message = "消息类型不能为空")
    private String msgType;

    /**
     * 数据集合
     */
    private Map<String, Object> dataMap;

}
