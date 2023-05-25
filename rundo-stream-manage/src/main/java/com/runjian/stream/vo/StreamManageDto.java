package com.runjian.stream.vo;

import com.runjian.common.constant.MsgType;
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

    public StreamManageDto(Long dispatchId, String streamId, MsgType msgType, Long outTime){
        this.dispatchId = dispatchId;
        this.streamId = streamId;
        this.msgType = msgType.getMsg();
        this.outTime = outTime;
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
     * 消息类型
     */
    private String msgType;

    /**
     * 过期时间
     */
    private Long outTime = 15000L;

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
