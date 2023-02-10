package com.runjian.parsing.service.north;

import com.runjian.common.config.response.CommonResponse;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author Miracle
 * @date 2023/2/10 10:18
 */
public interface StreamNorthService {

    /**
     * 停止播放北向接口
     * @param streamId 流id
     * @param response 异步返回体
     */
    void streamNorthStopPlay(Long dispatchId, String streamId, DeferredResult<CommonResponse<?>> response);

    /**
     * 开启录像北向接口
     * @param streamId 流id
     * @param response 异步返回体
     */
    void streamNorthStartRecord(Long dispatchId, String streamId, DeferredResult<CommonResponse<?>> response);

    /**
     * 停止录像北向接口
     * @param streamId 流id
     * @param response 异步返回体
     */
    void streamNorthStopRecord(Long dispatchId, String streamId, DeferredResult<CommonResponse<?>> response);
}
