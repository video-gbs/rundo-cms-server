package com.runjian.alarm.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Miracle
 * @date 2023/9/11 9:11
 */
@Data
public class AlarmManageDto {

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 过期时间
     */
    private Long outTime = 15000L;

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
