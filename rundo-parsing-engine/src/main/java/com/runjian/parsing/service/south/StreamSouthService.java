package com.runjian.parsing.service.south;

import com.runjian.common.config.response.CommonResponse;

import java.util.Map;

/**
 * @author Miracle
 * @date 2023/2/9 15:14
 */
public interface StreamSouthService {

    /**
     * 流播放处理器
     */
    void streamPlayResult(Long dispatchId, Object data);

    /**
     * 流关闭处理器
     * @param streamId 流id
     */
    void streamClose(Long dispatchId, Object data);

    /**
     * 停止播放接口
     * @param taskId 流id
     * @param dataMap
     */
    void streamStopPlay(Long taskId, Object data);

    /**
     * 开启录像接口
     * @param taskId 流id
     * @param dataMap
     */
    void streamStartRecord(Long taskId, Object data);

    /**
     * 停止录像接口
     * @param taskId 流id
     * @param dataMap
     */
    void streamStopRecord(Long taskId, Object data);

    /**
     * 检测流录像状态
     * @param taskId 任务id
     * @param dataMap 数据体
     */
    void streamCheckRecord(Long taskId, Object data);

    /**
     * 检测录像流
     * @param taskId 任务id
     * @param dataMap 数据体
     */
    void streamCheckStream(Long taskId, Object data);

    /**
     * 通用消息返回
     * @param taskId
     * @param dataMap
     */
    void customEvent(Long taskId, Object data);

    /**
     * 错误消息返回
     * @param taskId
     * @param response
     */
    void errorEvent(Long taskId, CommonResponse<?> response);
}
