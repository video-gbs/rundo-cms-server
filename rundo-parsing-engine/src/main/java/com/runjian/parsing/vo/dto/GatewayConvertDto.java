package com.runjian.parsing.vo.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author Miracle
 * @date 2023/1/28 10:31
 */
@Data
public class GatewayConvertDto {

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 通道id
     */
    private String channelId;

    /**
     * 其他数据
     */
    private Map<String, Object> dataMap;


}
