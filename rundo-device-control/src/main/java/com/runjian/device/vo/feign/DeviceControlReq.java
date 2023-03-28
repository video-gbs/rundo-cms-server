package com.runjian.device.vo.feign;



import com.runjian.common.constant.MsgType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备添加请求体
 * @author Miracle
 * @date 2023/1/9 15:11
 */
@Data
@NoArgsConstructor
public class DeviceControlReq {

    public DeviceControlReq(Long gatewayId, Long deviceId, Long channelId, MsgType msgType, Long outTime){
        this.gatewayId = gatewayId;
        this.deviceId = deviceId;
        this.channelId = channelId;
        this.msgType = msgType.getMsg();
        this.outTime = outTime;
    }

    /**
     * 网关ID
     */
    @Range(min = 1, message = "非法网关id")
    private Long gatewayId;

    /**
     * 设备ID
     */
    @Range(min = 1, message = "非法设备id")
    private Long deviceId;

    /**
     * 通道id
     */
    @Range(min = 1, message = "非法通道id")
    private Long channelId;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 过期时间
     */
    private Long outTime = 10L;

    /**
     * 数据集合
     */
    private Map<String, Object> dataMap = new HashMap<>();

    public void putData(String key, Object value){
        dataMap.put(key, value);
    }

    public void putAllData(Map<String, Object> mapData){
        dataMap.putAll(mapData);
    }

}
