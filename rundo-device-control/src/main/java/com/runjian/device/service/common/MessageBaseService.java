package com.runjian.device.service.common;

import com.runjian.device.constant.SubMsgType;

import java.util.Map;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/2/23 11:05
 */
public interface MessageBaseService {

    /**
     * 消息分发
     * @param subMsgType
     * @param dataMap
     */
    void msgDistribute(SubMsgType subMsgType, Map<Long, Object> dataMap);

    /**
     * 检查消息是否消费完成
     * @param subMsgType
     * @return
     */
    boolean checkMsgConsumeFinish(SubMsgType subMsgType, Set<Object> ids);

}
