package com.runjian.parsing.service.north;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.MsgType;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

/**
 * @author Miracle
 * @date 2023/2/10 10:18
 */
public interface StreamNorthService {


    /**
     * 通用处理方法
     * @param dispatchId 调度服务id
     * @param streamId 流id
     * @param mapData 数据
     * @param msgType 消息类型
     * @param response 异步返回体
     */
    void customEvent(Long dispatchId, String streamId, Map<String, Object> mapData, MsgType msgType, DeferredResult<CommonResponse<?>> response);
}
