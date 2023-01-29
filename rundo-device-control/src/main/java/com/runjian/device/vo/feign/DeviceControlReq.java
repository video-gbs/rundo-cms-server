package com.runjian.device.vo.feign;



import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备添加请求体
 * @author Miracle
 * @date 2023/1/9 15:11
 */
@Data
public class DeviceControlReq {

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 网关ID
     */
    private Long gatewayId;

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 数据集合
     */
    private Map<String, Object> dataMap = new HashMap<>();

    public void putData(String key, Object value){
        dataMap.put(key, value);
    }

}
