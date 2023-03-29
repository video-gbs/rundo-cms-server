package com.runjian.parsing.service.protocol;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.IdType;
import com.runjian.common.constant.MsgType;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

/**
 * 南向协议定义
 * @author Miracle
 * @date 2023/1/17 14:19
 */
public interface NorthProtocol {

    String DEFAULT_PROTOCOL = "DEFAULT";

    /**
     * 规范化：获取默认的协议处理器,这个方法必须复写
     * @return
     */
    String getProtocolName();

    /**
     * 消息分发
     * @param msgType
     * @param mainId
     * @param idType
     * @param dataMap
     * @param response
     */
    void msgDistribute(MsgType msgType, Long mainId, IdType idType, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response);


    /**
     * 通用自定义方法，只进行id数据转换
     * @param mainId 主要id
     * @param idType id类型
     * @param msgType 自定义消息类型
     * @param dataMap 数据集合
     * @param response 异步返回体
     */
    void customEvent(Long mainId, IdType idType, String msgType, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response);

}
