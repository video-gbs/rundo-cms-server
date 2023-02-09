package com.runjian.parsing.service;

import com.runjian.common.config.response.CommonResponse;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author Miracle
 * @date 2023/2/9 15:14
 */
public interface StreamManageService {



    /**
     * 流播放处理器
     */
    void streamSouthPlayResult(String streamId, Object data);

    /**
     * 流关闭处理器
     * @param streamId 流id
     */
    void streamSouthClose(String streamId);

    /**
     * 停止播放北向接口
     * @param streamId 流id
     * @param response 异步返回体
     */
    void streamNorthStopPlay(String streamId, DeferredResult<CommonResponse<?>> response);

    /**
     * 停止播放南向接口
     * @param streamId 流id
     * @param response 异步返回体
     */
    void streamSouthStopPlay(Long taskId, Object data);

    /**
     * 开启录像北向接口
     * @param streamId 流id
     * @param response 异步返回体
     */
    void streamNorthStartRecord(String streamId, DeferredResult<CommonResponse<?>> response);

    /**
     * 停止录像北向接口
     * @param streamId 流id
     * @param response 异步返回体
     */
    void streamSouthStartRecord(Long taskId, Object data);

    /**
     * 停止录像北向接口
     * @param streamId 流id
     * @param response 异步返回体
     */
    void streamNorthStopRecord(String streamId, DeferredResult<CommonResponse<?>> response);

    /**
     * 停止录像南向接口
     * @param streamId 流id
     * @param response 异步返回体
     */
    void streamSouthStopRecord(Long taskId, Object data);
}
