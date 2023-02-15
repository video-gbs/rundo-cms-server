package com.runjian.stream.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Miracle
 * @date 2023/2/9 15:29
 */
@Data
@NoArgsConstructor
public class StreamManageDto {

    public StreamManageDto(Long dispatchId, String streamId){
        this.dispatchId = dispatchId;
        this.streamId = streamId;
    }

    /**
     * 调度服务id
     */
    private Long dispatchId;

    /**
     * 流id
     */
    private String streamId;

    /**
     * 其他数据
     */
    private Map<String, Object> dataMap = new HashMap<>();

    public void put(String key, Object value){
        this.dataMap.put(key, value);
    }

    public void putAll(Map<String, Object> dataMap){
        this.dataMap.putAll(dataMap);
    }
}
