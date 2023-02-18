package com.runjian.stream.service.common;

import com.runjian.common.utils.CircleArray;

/**
 * @author Miracle
 * @date 2023/2/14 9:49
 */
public interface StreamBaseService {

    /**
     * 心跳时钟
     */
    CircleArray<String> prepareStreamOutTimeArray = new CircleArray<>(600);

    /**
     * 系统启动执行
     */
    void systemStart();

    /**
     * 检测超时未成功播放的流
     */
    void checkOutTimeStream();

    /**
     * 检测播放中的流状态
     */
    void checkPlayingStream();

    /**
     * 检查录像状态
     */
    void checkRecordState();

    /**
     * 重启清空所有正在播放的流
     */
    void initClearPrepareStream();

    /**
     * 检测流播放状态
     */
    void checkStreamState();
}
