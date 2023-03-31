package com.runjian.device.vo.feign;



import com.runjian.common.constant.IdType;
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

    public DeviceControlReq(Long mainId, IdType idType, MsgType msgType, Long outTime){
        this.mainId = mainId;
        this.idType = idType.getCode();
        this.msgType = msgType.getMsg();
        this.outTime = outTime;
    }

    /**
     * 主id
     */
    private Long mainId;

    /**
     * id类型
     * @see IdType
     */
    private Integer idType;

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
