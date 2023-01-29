package com.runjian.parsing.protocol;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.parsing.constant.IdType;
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
     * 规范化：设备同步
     * @param deviceId 设备id
     * @param dataMap 数据结合
     * @param response 请求返回体
     */
    void deviceSync(Long deviceId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response);

    /**
     * 规范化：主动添加设备
     * @param gatewayId 网关id
     * @param dataMap 数据结合
     * @param response 请求返回体
     */
    void deviceAdd(Long gatewayId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response);

    /**
     * 规范化：删除设备
     * @param deviceId 设备id
     */
    void deviceDelete(Long deviceId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response);

    /**
     * 规范化：通道同步
     * @param deviceId 设备id
     */
    void channelSync(Long deviceId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response);

    /**
     * 规范化：通道播放
     * @param channelId 通道id
     * @param dataMap 数据集合
     * @param response 异步返回体
     */
    void channelPlay(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response);

    /**
     * 规范化：获取通道回放数据
     * @param channelId 通道id
     * @param dataMap 数据集合
     * @param response 异步返回体
     */
    void getChannelRecord(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response);

    /**
     * 规范化：通道回放
     * @param channelId 通道id
     * @param dataMap 数据集合
     * @param response 异步返回体
     */
    void channelPlayback(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response);

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
