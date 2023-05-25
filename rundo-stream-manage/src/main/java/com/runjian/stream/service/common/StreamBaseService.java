package com.runjian.stream.service.common;

import com.runjian.common.utils.CircleArray;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/2/14 9:49
 */
public interface StreamBaseService {

    /**
     * 准备中的流过期时钟
     */
    CircleArray<String> STREAM_OUT_TIME_ARRAY = new CircleArray<>(600);

    /**
     * 系统启动执行
     */
    void init();

    /**
     * 检测播放中的流状态
     */
    void checkPlayingStream();

    /**
     * 检测流播放状态
     */
    void checkStreamState(LocalDateTime nowTime);

    /**
     * 检查录像状态
     */
    void checkRecordState(LocalDateTime nowTime);


}
