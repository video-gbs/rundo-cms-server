package com.runjian.parsing.service.south;

import com.runjian.common.config.response.CommonResponse;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author Miracle
 * @date 2023/2/9 15:14
 */
public interface StreamSouthService {



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
     * 停止播放南向接口
     * @param taskId 流id
     * @param data
     */
    void streamSouthStopPlay(Long taskId, Object data);



    /**
     * 停止录像北向接口
     * @param taskId 流id
     * @param data
     */
    void streamSouthStartRecord(Long taskId, Object data);



    /**
     * 停止录像南向接口
     * @param taskId 流id
     * @param data
     */
    void streamSouthStopRecord(Long taskId, Object data);

    /**
     * 通用消息返回
     * @param taskId
     * @param dataMap
     */
    void customEvent(Long taskId, Object dataMap);

    /**
     * 错误消息返回
     * @param taskId
     * @param response
     */
    void errorEvent(Long taskId, CommonResponse<?> response);
}
