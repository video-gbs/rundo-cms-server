package com.runjian.device.service.north;

import com.runjian.device.entity.MessageInfo;
import com.runjian.device.vo.response.PostMessageSubRsp;

import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/5/16 14:33
 */
public interface MessageNorthService {


    /**
     * 订阅消息
     */
    List<PostMessageSubRsp> SubMsg(String serviceName, Set<String> msgTypes);

    /**
     * 取消订阅消息
     */
    void cancelSubMsg(Set<String> msgHandles);
}
