package com.runjian.parsing.service.protocol;

import com.runjian.common.config.response.CommonResponse;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

/**
 * 协议定义
 * @author Miracle
 * @date 2023/1/17 14:19
 */
public interface Protocol {

    String DEFAULT_PROTOCOL = "DEFAULT";

    /**
     * 获取默认的协议处理器,这个方法必须复写
     * @return
     */
    String getProtocolName();

    /**
     * 设备同步
     * @param deviceId 设备id
     */
    void deviceSync(Long deviceId, DeferredResult<CommonResponse<?>> response);

    /**
     * 主动添加设备
     * @param gatewayId 网关id
     * @param dataMap 数据结合
     * @param response 请求返回体
     */
    void deviceAdd(Long gatewayId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response);

    /**
     * 删除设备
     * @param deviceId 设备id
     */
    void deviceDelete(Long deviceId, DeferredResult<CommonResponse<?>> response);

    /**
     * 通道同步
     * @param deviceId 设备id
     */
    void channelSync(Long deviceId, DeferredResult<CommonResponse<?>> response);

    /**
     * 通道播放
     * @param channelId 通道id
     * @param dataMap 数据集合
     * @param response 异步返回体
     */
    void channelPlay(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response);

    /**
     * 通道回放
     * @param channelId 通道id
     * @param dataMap 数据集合
     * @param response 异步返回体
     */
    void channelPlayback(Long channelId, Map<String, Object> dataMap, DeferredResult<CommonResponse<?>> response);

}
