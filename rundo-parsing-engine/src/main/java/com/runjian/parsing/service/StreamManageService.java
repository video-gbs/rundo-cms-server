package com.runjian.parsing.service;

/**
 * @author Miracle
 * @date 2023/2/9 15:14
 */
public interface StreamManageService {

    /**
     * 流播放处理器
     */
    void streamPlayResult(String streamId, Object data);

    /**
     * 流关闭处理器
     * @param streamId 流id
     */
    void streamClose(String streamId);
}
